package frc.robot.sensors.anglesensor;

public class MockAngleSensor extends AngleSensor {

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public void reset() {
    System.out.println("trying to reset");
  }
}