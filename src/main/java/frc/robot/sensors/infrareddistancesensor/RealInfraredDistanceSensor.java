package frc.robot.sensors.infrareddistancesensor;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

public class RealInfraredDistanceSensor extends RealSensorBase implements InfraredDistanceSensor {
  private AnalogInput m_infraredDistanceSensor;
  private String m_name;

  public RealInfraredDistanceSensor(String configString) {
    m_infraredDistanceSensor = new AnalogInput(
        Config4905.getConfig4905().getSensorConfig().getInt("sensors." + configString + ".port"));
    m_name = configString;
  }

  public double getInfraredDistance() {
    return m_infraredDistanceSensor.getVoltage();
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber(m_name, getInfraredDistance());
  }
}