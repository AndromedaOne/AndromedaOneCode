package frc.robot.sensors.colorSensor;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Config4905;

public class ColorSensor {

  AnalogInput m_colorSensor;

  public ColorSensor(String configString) {
    m_colorSensor = new AnalogInput(
        Config4905.getConfig4905().getSensorConfig().getInt("sensors." + configString + ".port"));
  }

  public double getValue() {
    return m_colorSensor.getVoltage();
  }

}
