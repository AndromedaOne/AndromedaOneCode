// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.showBotCannon.CannonBase;

public class ResetCannon extends Command {
  private CannonBase m_cannon;

  /** Creates a new ResetCannon. */
  public ResetCannon() {
    m_cannon = Robot.getInstance().getSubsystemsContainer().getShowBotCannon();
    addRequirements(m_cannon.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_cannon.reset();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
