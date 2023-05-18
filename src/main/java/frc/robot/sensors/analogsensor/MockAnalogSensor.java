package frc.robot.sensors.analogsensor;

public class MockAnalogSensor implements AnalogSensor {

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public void reset() {
    System.out.println("trying to reset");
  }
}