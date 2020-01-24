package frc.robot.sensors.infrareddistancesensor;

public class MockInfraredDistanceSensor extends InfraredDistanceSensor {
  double infraredDistance;

  public double getInfraredDistance() {
    return infraredDistance;
  }

  @Override
  public void putSensorOnLiveWindow(String subsystemNameParam, String sensorNameParam) {
    System.out.println("The sensor named: " + sensorNameParam + " Does not exist.");
  }
}