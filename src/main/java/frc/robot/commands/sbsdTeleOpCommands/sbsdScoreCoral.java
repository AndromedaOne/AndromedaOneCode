// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdcoralendeffector.CoralIntakeEjectBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class sbsdScoreCoral extends Command {
  /** Creates a new sbsdScoreCoral. */
  private CoralIntakeEjectBase m_endEffector;

  public sbsdScoreCoral() {
    m_endEffector = Robot.getInstance().getSubsystemsContainer().getSBSDCoralIntakeEjectBase();
    addRequirements(m_endEffector.getSubsystemBase());
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (Robot.getInstance().isAutonomous()) {
      m_endEffector.setEjectState();
    }

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
    if (Robot.getInstance().isAutonomous()) {
      return m_endEffector.hasScored();
    }
    return true;
  }
}
