package frc.robot.sensors.anglesensor;

public class MockAngleSensor extends AngleSensor {

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public void reset() {
    System.out.println("trying to reset");
  }

  @Override
  public void putSensorOnLiveWindow(String subsystemNameParam, String sensorNameParam) {
    System.out.println("Cannot put sensor named: " + sensorNameParam + "On LiveWindow");
  }
}