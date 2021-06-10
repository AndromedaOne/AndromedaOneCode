package frc.robot.subsystems.romishooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class RomiShooterBase extends SubsystemBase {

  public abstract void setSpeed(double speed);

  public abstract boolean isShooterRunning();
}
