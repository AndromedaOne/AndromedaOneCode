package frc.robot.sensors.limitswitchsensor;

public abstract class LimitSwitchSensor {

  /**
   * @return trues if the limit switch is at its limit
   */
  public abstract boolean isAtLimit();

  public class IsAtLimitException extends Exception {

    private static final long serialVersionUID = 1L;

  }
}