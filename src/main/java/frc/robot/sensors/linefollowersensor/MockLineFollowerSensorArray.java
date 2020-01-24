package frc.robot.sensors.linefollowersensor;

public class MockLineFollowerSensorArray extends LineFollowerSensorBase {

  public MockLineFollowerSensorArray() {
    // TODO Add to config
    super("mockLineFollower");
  }

  @Override
  public void getSensorReading(int[] readingBuf) {
    // This should be called once every 20ms
    System.out.println("Reading Mock Line Sensor");
  }

}