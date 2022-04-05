// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.telemetries.Trace;

public class ExtendShooterArms extends CommandBase {
  private ShooterAlignmentBase m_shooterAlignmentBase;
  private int m_counter = 0;

  public ExtendShooterArms(ShooterAlignmentBase shooterAlignmentBase) {
    addRequirements(shooterAlignmentBase);
    m_shooterAlignmentBase = shooterAlignmentBase;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooterAlignmentBase.extendShooterArms();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    ++m_counter;
    if (m_counter > 400) {
      return true;
    }
   return false;
  }
}
