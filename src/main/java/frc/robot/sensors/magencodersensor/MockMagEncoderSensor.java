package frc.robot.sensors.magencodersensor;

public class MockMagEncoderSensor extends MagEncoderSensor {

  @Override
  public double getDistanceTicks() {
    return 0;
  }

  @Override
  public void reset() {
    System.out.println("Trying to reset the encoder sensor");
  }

  @Override
  public void resetTo(double value) {
    System.out.println("Trying to reset the encoder to: " + value);
  }

  @Override
  public double getVelocity() {
    return 0;
  }

}