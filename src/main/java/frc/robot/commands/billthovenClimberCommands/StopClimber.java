// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billClimber.BillClimberBase;

public class StopClimber extends Command {
  private BillClimberBase m_climber;

  /** Creates a new StopClimber. */
  public StopClimber(BillClimberBase climber) {
    m_climber = climber;
    addRequirements(m_climber.getSubsystemBase());
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climber.stopWinch();
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
