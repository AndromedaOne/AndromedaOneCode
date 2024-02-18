package frc.robot.sensors.distanceSensor.ultrasonicsensor;

public class MockUltrasonicSensorPair implements UltrasonicSensor {
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