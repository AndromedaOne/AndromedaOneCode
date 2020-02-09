package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShooterBase extends SubsystemBase {

  /**
   * Gets the average velocity from both shooting wheels <b>NOT</b> from the
   * kicker wheel
   */
  public abstract double getShooterWheelVelocity();

  /**
   * Gets the average velocity from the kicker wheel <b>NOT</b> the shooter wheels
   */
  public abstract double getShooterSeriesVelocity();

  /**
   * This is the percent vbus power for the shooting wheels
   * 
   * @param power
   */
  public abstract void setShooterWheelPower(double power);

  /**
   * This is the percent vbus power for the kicker wheel
   */
  public abstract void setShooterSeriesPower(double power);

  /**
   * This returns the velocity in rpm
   */
  public abstract double getShooterPower();

  /**
   * This returns the velocity in rpm
   */
  public abstract double getSeriesPower();

  public abstract void openShooterHood();

  public abstract void closeShooterHood();

  public abstract boolean isShooterHoodOpen();

  /**
   * This is <b>Only</b> to be called by the shooter commands
   */
  public abstract void setShooterPIDIsReady(boolean isReady);

  /**
   * This is <b>Only</b> to be called by the shooter commands
   */
  public abstract void setSeriesPIDIsReady(boolean isReady);

  /**
   * This is <b>Only</b> to be called by the shooter commands
   */
  public abstract void setShooterIsIdle(boolean isIdle);

  /**
   * This will return true if the shooters current velocity is on target with the
   * desired velocity
   * 
   * @return
   */
  public abstract boolean shooterIsReady();
}