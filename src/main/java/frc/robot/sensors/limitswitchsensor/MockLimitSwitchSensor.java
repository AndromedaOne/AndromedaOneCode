package frc.robot.sensors.limitswitchsensor;

public class MockLimitSwitchSensor extends LimitSwitchSensor {

  @Override
  public boolean isAtLimit() {
    return false;
  }

  @Override
  public void putSensorOnLiveWindow(String subsystemNameParam, String sensorNameParam) {
    System.out.println("Cannot put the sensor named: " + sensorNameParam + "on live window because it does not exist");
  }
}