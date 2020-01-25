package frc.robot.sensors.ultrasonicsensor;

public class MockUltrasonicSensorPair extends UltrasonicSensor {
  public MockUltrasonicSensorPair() {
  }

  public double getLeftUltrasonicDistanceInches() {
    return 0;
  }

  public double getRightUltrasonicDistanceInches() {
    return 0;
  }

  @Override
  public double getDistanceInches() {
    return 0;
  }
}