package frc.robot.sensors.encodersensor.sparkmaxencodersensor;

public class RealSparkMaxEncoderSensor extends SparkMaxEncoderSensor {

  @Override
  public double getDistanceTicks() {
    return 0;
  }

  @Override
  public double getDistanceInches() {
    return getDistanceTicks() / ticksPerInch;
  }

  @Override
  public double getRate() {
    return 0;
  }

  @Override
  public void reset() {

  }

}