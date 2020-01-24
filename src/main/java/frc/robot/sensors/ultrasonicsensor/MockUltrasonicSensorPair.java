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

  @Override
  public void putSensorOnLiveWindow(String subsystemNameParam, String sensorNameParam) {
    System.out.println("Cannot put the sensor named: " + sensorNameParam + "on live window because it does not exist");
    System.out.println("Mock Ultrasonic");
  }
}