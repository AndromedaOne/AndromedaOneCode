package frc.robot.sensors.ultrasonicsensor;

public class MockUltrasonicSensor extends UltrasonicSensor {

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