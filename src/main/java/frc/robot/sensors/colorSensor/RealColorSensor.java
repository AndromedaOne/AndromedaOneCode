package frc.robot.sensors.colorSensor;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

public class RealColorSensor extends RealSensorBase implements ColorSensorBase {
  AnalogInput m_colorSensor;
  String m_name;

  public RealColorSensor(String configString) {
    m_colorSensor = new AnalogInput(
        Config4905.getConfig4905().getSensorConfig().getInt("sensors." + configString + ".port"));
    m_name = configString;
  }

  public double getReflectedLightIntensity() {
    return m_colorSensor.getVoltage();
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber(m_name, getReflectedLightIntensity());
  }
}
