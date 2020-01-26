package frc.robot.sensors.analogsensor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Config4905;

public class RealAnalogSensor extends AnalogSensor {
  private AnalogInput angleSensor;
  private boolean useWrapAround = false;
  private Config conf;

  public RealAnalogSensor(int port, String configString) {
    angleSensor = new AnalogInput(port);
    this.conf = Config4905.getConfig4905().getSensorConfig();
    this.useWrapAround = this.conf.getBoolean("sensors." + configString + "useWrapAround");
  }

  @Override
  public double getAngle() {
    double sensorValue = angleSensor.getVoltage();
    if (sensorValue < 0.94 && useWrapAround) {
      sensorValue = (sensorValue - 0.316) + 2.85;
    }
    return sensorValue;
  }

  @Override
  public void reset() {
  }
}