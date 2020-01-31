package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShooterBase extends SubsystemBase {

  public abstract double getShooterVelocity();
  
  public abstract void setShooterPower(double power);

  public abstract double getShooterPower();

  public abstract void openShooterHood();

  public abstract void closeShooterHood();
}