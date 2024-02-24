// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.billClimber.BillClimberBase;
import frc.robot.telemetries.Trace;

public class EnableClimberMode extends Command {
  /** Creates a new EnableClimberMode. */
  private BillClimberBase m_climber;

  public EnableClimberMode(BillClimberBase climber) {
    m_climber = climber;
    addRequirements(m_climber.getSubsystemBase());
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    CommandScheduler.getInstance().removeDefaultCommand(m_climber.getSubsystemBase());
    CommandScheduler.getInstance().setDefaultCommand(m_climber.getSubsystemBase(),
        new RunBillClimber(m_climber, false));
    Trace.getInstance().logCommandInfo(this, "Enable Climber Mode Ran");
    Trace.getInstance().logCommandInfo(this,
        CommandScheduler.getInstance().getDefaultCommand(m_climber.getSubsystemBase()).getName());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
