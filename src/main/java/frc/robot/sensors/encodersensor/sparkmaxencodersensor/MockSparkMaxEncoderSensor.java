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
  public double getVelocity() {
    return 0;
  }

}