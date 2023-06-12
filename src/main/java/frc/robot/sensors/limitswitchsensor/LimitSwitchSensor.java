package frc.robot.sensors.limitswitchsensor;

public interface LimitSwitchSensor {

  /**
   * @return trues if the limit switch is at its limit
   */
  public abstract boolean isAtLimit();

}