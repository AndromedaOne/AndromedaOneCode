package frc.robot.sensors.limitswitchsensor;

import frc.robot.sensors.SensorBase;

public abstract class LimitSwitchSensor extends SensorBase {

  /**
   * @return trues if the limit switch is at its limit
   */
  public abstract boolean isAtLimit();

  public class IsAtLimitException extends Exception {

    private static final long serialVersionUID = 1L;

  }
}