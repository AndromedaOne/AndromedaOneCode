// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdclimber.ClimberMode;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ClimberDefaultCommand extends Command {
  /** Creates a new ClimberDefaultCommand. */
  public ClimberDefaultCommand() {
    addRequirements(
        Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase().getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase().stop();
    if (!ClimberMode.getInstance().getInClimberMode()) {
      Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase().setServoInitialPosition();
    } else {
      Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase().unlatchTrident();
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase().stop();
    if (!ClimberMode.getInstance().getInClimberMode()) {
      Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase().setServoInitialPosition();
    } else {
      Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase().unlatchTrident();
    }
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
