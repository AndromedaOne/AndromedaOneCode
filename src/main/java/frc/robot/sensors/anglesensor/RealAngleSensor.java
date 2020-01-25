package frc.robot.sensors.anglesensor;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Robot;

public class RealAngleSensor extends AngleSensor {
  private AnalogInput angleSensor;
  private boolean useWrapAround = false;

  public RealAngleSensor(int port) {
    angleSensor = new AnalogInput(port);
    useWrapAround = Robot.getConfig().getBoolean("ports.intake.useIntakeWrapAround");
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