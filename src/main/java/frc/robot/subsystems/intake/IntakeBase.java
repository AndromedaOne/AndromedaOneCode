package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class IntakeBase extends SubsystemBase {
  public abstract void runIntake(double speed);

  public abstract void stopIntake();

  public abstract void deployIntake();

  public abstract void retractIntake();
}