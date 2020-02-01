package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShooterBase extends SubsystemBase {

  /**
   * Gets the average velocity from both shooting wheels Not from the kicker wheel
   */
  public abstract double getShooterVelocity();

  public abstract void setShooterPower(double power);

  public abstract double getShooterPower();

  public abstract void openShooterHood();

  public abstract void closeShooterHood();

  public abstract boolean isShooterHoodOpen();

  /**
   * This is only to be called by the shooter commands
   */
  public abstract void setPIDIsReady(boolean isReady);

  /**
   * This will return true if the shooters current velocity is
   * on target with the desired velocity 
   * @return
   */
  public abstract boolean shooterIsReady();
}