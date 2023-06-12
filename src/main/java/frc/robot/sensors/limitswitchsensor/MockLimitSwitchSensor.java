package frc.robot.sensors.limitswitchsensor;

public class MockLimitSwitchSensor implements LimitSwitchSensor {

  @Override
  public boolean isAtLimit() {
    return false;
  }

}