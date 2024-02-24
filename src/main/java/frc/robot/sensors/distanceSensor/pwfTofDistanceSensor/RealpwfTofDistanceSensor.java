package frc.robot.sensors.distanceSensor.pwfTofDistanceSensor;

import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;

public class RealpwfTofDistanceSensor extends RealSensorBase implements DistanceSensorBase {
  private TimeOfFlight m_tof;
  private Config m_sensorConfig = Config4905.getConfig4905().getSensorConfig();

  public RealpwfTofDistanceSensor() {
    m_tof = new TimeOfFlight(m_sensorConfig.getInt("sensors.rearTof.port"));
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
    m_tof.setRangingMode(rangingMode, m_sensorConfig.getInt("sensors.rearTof.sampleTime"));
    m_tof.setRangeOfInterest(m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.topLeftX"),
        m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.topLeftY"),
        m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.bottomRightX"),
        m_sensorConfig.getInt("sensors.rearTof.rangeOfInterest.bottomRightY"));
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber("TOF Distance mm", getDistance_mm());
    SmartDashboard.putNumber("TOF Dintance Inches", getDistance_Inches());
  }

  @Override
  public double getDistance_mm() {
    return m_tof.getRange();
  }

  @Override
  public double getDistance_Inches() {
    return m_tof.getRange()/25.4;
  }
}
