package frc.robot.sensors.encodersensor.sparkmaxencodersensor;

public class MockSparkMaxEncoderSensor extends SparkMaxEncoderSensor {

  @Override
  public double getDistanceTicks() {
    return 0;
  }

  @Override
  public double getDistanceInches() {
    return 0;
  }

  @Override
  public double getRate() {
    return 0;
  }

  @Override
  public void reset() {
    // Attempting to reset the non-existent encoder
  }

}