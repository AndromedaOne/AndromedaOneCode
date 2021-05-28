package frc.robot.sensors.colorSensor;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Config4905;

public class RealColorSensor extends ColorSensorBase {

  AnalogInput m_colorSensor;

  public RealColorSensor(String configString) {
    m_colorSensor = new AnalogInput(
        Config4905.getConfig4905().getSensorConfig().getInt("sensors." + configString + ".port"));
  }

  public double getReflectedLightIntensity() {
    return m_colorSensor.getVoltage();
  }

}
