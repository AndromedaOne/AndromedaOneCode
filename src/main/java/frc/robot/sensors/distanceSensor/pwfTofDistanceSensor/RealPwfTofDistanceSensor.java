package frc.robot.sensors.distanceSensor.pwfTofDistanceSensor;

import java.util.function.DoubleSupplier;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import com.playingwithfusion.TimeOfFlight.Status;
import com.typesafe.config.Config;

import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;
import frc.robot.telemetries.Trace;

public class RealPwfTofDistanceSensor extends RealSensorBase implements DistanceSensorBase {
  private TimeOfFlight m_tof;
  private Config m_sensorConfig = Config4905.getConfig4905().getSensorConfig();
  private String m_sensorName;
  private LinearFilter m_linearFilter;
  private boolean m_useLF = false;
  private double m_offsetInches = 0.0;
  private double m_facingAngle = 0.0;
  private double m_lastValidValue = 50 * 25.4;

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
    m_useLF = m_sensorConfig.getBoolean("sensors." + sensorName + ".useLinearFilter");
    if (m_sensorConfig.getInt("sensors." + sensorName + ".numberOfTaps") == 0) {
      m_useLF = false;
    }
    if (m_useLF) {
      m_linearFilter = LinearFilter
          .movingAverage(m_sensorConfig.getInt("sensors." + sensorName + ".numberOfTaps"));
    }
    m_offsetInches = m_sensorConfig.getDouble("sensors." + m_sensorName + ".sensorOffset_inches");
    m_facingAngle = m_sensorConfig.getDouble("sensors." + m_sensorName + ".facingAngle");
    m_lastValidValue -= (m_offsetInches * 25.4);
  }

  @Override
  protected void periodicUpdate() {
    if (m_useLF) {
      m_linearFilter.calculate(getUnfilteredDistance_Inches());
      SmartDashboard.putNumber(m_sensorName + "/TOF Filtered Distance mm", getDistance_mm());
      SmartDashboard.putNumber(m_sensorName + "/TOF Filtered Distance Inches",
          getDistance_Inches());
    }
    SmartDashboard.putNumber(m_sensorName + "/TOF Distance mm", getUnfilteredDistance_mm());
    SmartDashboard.putNumber(m_sensorName + "/TOF Distance Inches", getUnfilteredDistance_Inches());
    SmartDashboard.putNumber(m_sensorName + "/TOF Standard Deviation Inches",
        getRangeSigma_inches());
    SmartDashboard.putNumber(m_sensorName + "/TOF Ambient Light Level", getAmbientLightLevel());
    SmartDashboard.putBoolean(m_sensorName + "/TOF Is Range Valid", isRangeValid());
    SmartDashboard.putString(m_sensorName + "/TOF Status", getStatus().toString());
    SmartDashboard.putString(m_sensorName + "/TOF Ranging Mode", getRangingMode().toString());
  }

  public double getUnfilteredDistance_mm() {
    return getLastValidValue() + (m_offsetInches * 25.4);
  }

  public double getUnfilteredDistance_Inches() {
    return (getLastValidValue() / 25.4) + m_offsetInches;
  }

  @Override
  public double getDistance_mm() {
    return getLastFilteredValue() * 25.4;
  }

  @Override
  public double getDistance_Inches() {
    return getLastFilteredValue();
  }

  private double getLastFilteredValue() {
    if (m_useLF) {
      return m_linearFilter.lastValue();
    }
    return getUnfilteredDistance_Inches();
  }

  // returns in millimeters
  private double getLastValidValue() {
    TimeOfFlight.Status status = m_tof.getStatus();
    if (status == Status.Valid) {
      m_lastValidValue = m_tof.getRange();
      return m_lastValidValue;
    } else if (status == Status.Invalid) {
      return (getLastFilteredValue() * 25.4) - (m_offsetInches * 25.4);
    } else if (status == Status.ReturnSignalLow) {
      // 50 (max value sensor can read in inches) * 25.4 (conversion #)
      return 1270 - (m_offsetInches * 25.4);
    } else if (status == Status.ReturnPhaseBad || status == Status.WrappedTarget
        || status == Status.SigmaHigh) {
      return m_lastValidValue;
    }
    Trace.getInstance()
        .logInfo("Non-expected status off " + m_sensorName + ", status = " + status.toString());
    return m_lastValidValue;
  }

  @Override
  public DoubleSupplier getDistanceInchesAsSupplier() {
    return () -> getDistance_Inches();
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
    return m_tof.getRangeSigma() / 25.4;
  }

  public TimeOfFlight.Status getStatus() {
    return m_tof.getStatus();
  }

  public TimeOfFlight.RangingMode getRangingMode() {
    return m_tof.getRangingMode();
  }

  @Override
  public DoubleSupplier getFacingAngle() {
    return () -> m_facingAngle;
  }

  @Override
  public boolean isSensorDetecting() {
    return !(m_tof.getStatus() == Status.ReturnSignalLow);
  }
}
