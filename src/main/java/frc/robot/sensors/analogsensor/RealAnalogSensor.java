package frc.robot.sensors.analogsensor;

import edu.wpi.first.wpilibj.AnalogInput;

public class RealAnalogSensor extends AnalogSensor {
  private AnalogInput angleSensor;
  private boolean useWrapAround = false;

  public RealAnalogSensor(int port) {
    angleSensor = new AnalogInput(port);

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