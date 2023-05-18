package frc.robot.sensors.analogsensor;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

public class RealAnalogSensor extends RealSensorBase implements AnalogSensor {
  private AnalogInput m_angleSensor;
  private boolean m_useWrapAround = false;
  private String m_name;

  public RealAnalogSensor(int port, String configString) {
    m_angleSensor = new AnalogInput(port);
    m_useWrapAround = Config4905.getConfig4905().getSensorConfig()
        .getBoolean("sensors." + configString + "useWrapAround");
    m_name = configString;
  }

  @Override
  public double getAngle() {
    double sensorValue = m_angleSensor.getVoltage();
    if (sensorValue < 0.94 && m_useWrapAround) {
      sensorValue = (sensorValue - 0.316) + 2.85;
    }
    return sensorValue;
  }

  @Override
  public void reset() {
  }

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber(m_name, getAngle());
  }
}