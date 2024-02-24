package frc.robot.sensors.distanceSensor.pwfTofDistanceSensor;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;

public class RealPwfTofDistanceSensor extends RealSensorBase implements DistanceSensorBase {
  private TimeOfFlight m_tof;
  private Config m_sensorConfig = Config4905.getConfig4905().getSensorConfig();

  public RealPwfTofDistanceSensor() {
    m_tof = new TimeOfFlight(m_sensorConfig.getInt("sensors.rearTof.port"));
    /* ranging mode distances
     * short < 1300mm (51.2in)
     * medium < 2700mm(106.3in)
     * long < 4000mm(157.5in)
     */
    RangingMode rangingMode;
    switch (m_sensorConfig.getString("sensors.rearTof.rangingMode")) {
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
    /* Sample time values
     * 20 ms is the minimum timing budget and can be used only in Short distance mode.
     * 33 ms is the minimum timing budget which can work for all distance modes.
     * 140 ms is the timing budget which allows the maximum distance of 4 m (in the dark on
     * a white chart) to be reached under Long distance mode
     */
    m_tof.setRangingMode(rangingMode, m_sensorConfig.getInt("sensors.rearTof.sampleTime"));
    /* The range of interest rows and columns must be greater or equal to zero 
     * and less than or equal to fifteen. 
     * The top left corner row/column must be smaller than the bottom right column/row. 
     * The region of interest must be at least four coulmns wide and four rows tall.
     */
    m_tof.setRangeOfInterest(m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.topLeftX"),
        m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.topLeftY"),
        m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.bottomRightX"),
        m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.bottomRightY"));
  }
    
  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber("TOF Distance mm", getDistance_mm());
    SmartDashboard.putNumber("TOF Dintance Inches", getDistance_Inches());
    SmartDashboard.putNumber("TOF Standad Deviation Inches", getRangeSigma_inches());
    SmartDashboard.putNumber("TOF Ambient Light Level", getAmbientLightLevel());
    SmartDashboard.putBoolean("TOF Is Range Valid", isRangeValid());
    SmartDashboard.putString("TOF Status", getStatus().toString());
    SmartDashboard.putString("TOF Ranging Mode", getRangingMode().toString());
  }

  @Override
  public double getDistance_mm() {
    return m_tof.getRange() + (m_sensorConfig.getInt("sensors.rearTof.sensorOffset_inches") * 25.4);
  }

  @Override 
  public double getDistance_Inches() {
    return (m_tof.getRange()/25.4) + (m_sensorConfig.getInt("sensors.rearTof.sensorOffset_inches"));
  }
  /* Determine if the last measurment was valid
   */
  public boolean isRangeValid() {
    return m_tof.isRangeValid();
  }
  /* Get ambient lighting level in mega counts per second.
   */
   public double getAmbientLightLevel() {
     return m_tof.getAmbientLightLevel();
   }
   /* Get the standard deviation of the distance measurment in millimeters
    */
   public double getRangeSigma_mm() {
    return m_tof.getRangeSigma();
   }
   /* Get the standard deviation of the distance measurment in inches
    */
   public double getRangeSigma_inches() {
    return m_tof.getRangeSigma()/25.4;
   }

   public TimeOfFlight.Status getStatus() {
    return m_tof.getStatus();
   }
   public TimeOfFlight.RangingMode getRangingMode() {
    return m_tof.getRangingMode();
   }
}
