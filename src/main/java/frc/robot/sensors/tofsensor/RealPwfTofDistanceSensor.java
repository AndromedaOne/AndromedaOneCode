package frc.robot.sensors.tofsensor;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import com.typesafe.config.Config;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

public class RealPwfTofDistanceSensor extends RealSensorBase implements ToFSensorBase {
  private TimeOfFlight m_tof;
  private Config m_sensorConfig = Config4905.getConfig4905().getSensorConfig();
  private String m_sensorName;
  private double m_offset;
  private LinearFilter m_linearFilter;
  private static final double m_conversionFromInchesToCm = 25.4;

  public RealPwfTofDistanceSensor(String sensorName) {
    m_sensorName = sensorName;
    m_tof = new TimeOfFlight(m_sensorConfig.getInt("sensors." + sensorName + ".port"));
    /*
     * ranging mode distances short < 1300mm (51.2in) medium < 2700mm(106.3in) long
     * < 4000mm(157.5in)
     */
    RangingMode rangingMode;
    switch (m_sensorConfig.getString("sensors." + sensorName + ".rangingMode")) {
    case "Long":
      rangingMode = RangingMode.Long;
      break;
    case "Medium":
      rangingMode = RangingMode.Medium;
      break;
    case "Short":
      rangingMode = RangingMode.Short;
      break;
    default:
      rangingMode = RangingMode.Long;
      break;
    }
    /*
     * Sample time values 20 ms is the minimum timing budget and can be used only in
     * Short distance mode. 33 ms is the minimum timing budget which can work for
     * all distance modes. 140 ms is the timing budget which allows the maximum
     * distance of 4 m (in the dark on a white chart) to be reached under Long
     * distance mode
     */
    m_tof.setRangingMode(rangingMode,
        m_sensorConfig.getInt("sensors." + sensorName + ".sampleTime"));

    m_offset = m_sensorConfig.getInt("sensors." + m_sensorName + ".sensorOffset_inches");
    /*
     * The range of interest rows and columns must be greater or equal to zero and
     * less than or equal to fifteen. The top left corner row/column must be smaller
     * than the bottom right column/row. The region of interest must be at least
     * four coulmns wide and four rows tall.
     */
    m_tof.setRangeOfInterest(
        m_sensorConfig.getInt("sensors." + sensorName + ".rangeOfInterest.topLeftX"),
        m_sensorConfig.getInt("sensors." + sensorName + ".rangeOfInterest.topLeftY"),
        m_sensorConfig.getInt("sensors." + sensorName + ".rangeOfInterest.bottomRightX"),
        m_sensorConfig.getInt("sensors." + sensorName + ".rangeOfInterest.bottomRightY"));
    m_linearFilter = LinearFilter
        .movingAverage(m_sensorConfig.getInt("sensors." + sensorName + ".numberOfTaps"));
  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber(m_sensorName + "/TOF Raw Distance mm", getRawDistance_mm());
    SmartDashboard.putNumber(m_sensorName + "/TOF Raw Distance Inches", getRawDistance_Inches());
    SmartDashboard.putNumber(m_sensorName + "/TOF Standard Deviation Inches",
        getRangeSigma_inches());
    SmartDashboard.putNumber(m_sensorName + "/TOF Ambient Light Level", getAmbientLightLevel());
    SmartDashboard.putBoolean(m_sensorName + "/TOF Is Range Valid", isRangeValid());
    SmartDashboard.putString(m_sensorName + "/TOF Status", getStatus().toString());
    SmartDashboard.putString(m_sensorName + "/TOF Ranging Mode", getRangingMode().toString());
    SmartDashboard.putNumber(m_sensorName + "/TOF linear filtered distance",
        getLinearFilterValue());
  }

  public double getRawDistance_mm() {
    return m_tof.getRange() + (m_offset * m_conversionFromInchesToCm);
  }

  public double getRawDistance_Inches() {
    return (m_tof.getRange() / m_conversionFromInchesToCm) + m_offset;
  }

  public double getDistance_mm() {
    return getLinearFilterValue() * m_conversionFromInchesToCm;
  }

  public double getDistance_Inches() {
    return getLinearFilterValue();
  }

  public double getLinearFilterValue() {
    return m_linearFilter.lastValue();
  }

  @Override
  public void update() {
    m_linearFilter.calculate(getRawDistance_Inches());
  }

  /*
   * Determine if the last measurment was valid
   */
  public boolean isRangeValid() {
    return m_tof.isRangeValid();
  }

  /*
   * Get ambient lighting level in mega counts per second.
   */
  public double getAmbientLightLevel() {
    return m_tof.getAmbientLightLevel();
  }

  /*
   * Get the standard deviation of the distance measurment in millimeters
   */
  public double getRangeSigma_mm() {
    return m_tof.getRangeSigma();
  }

  /*
   * Get the standard deviation of the distance measurment in inches
   */
  public double getRangeSigma_inches() {
    return m_tof.getRangeSigma() / m_conversionFromInchesToCm;
  }

  public TimeOfFlight.Status getStatus() {
    return m_tof.getStatus();
  }

  public TimeOfFlight.RangingMode getRangingMode() {
    return m_tof.getRangingMode();
  }
}
