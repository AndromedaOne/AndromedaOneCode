// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class VibrateRobot extends Command {
  private enum Direction {
    FORWARD, BACKWARD
  }

  private DriveTrainBase m_driveTrain;
  private Direction m_direction = Direction.FORWARD;

  public VibrateRobot(DriveTrainBase driveTrain) {
    m_driveTrain = driveTrain;
    addRequirements(driveTrain.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_direction == Direction.FORWARD) {
      m_driveTrain.vibrateRobot(0.25);
      m_direction = Direction.BACKWARD;
    } else {
      m_driveTrain.vibrateRobot(-0.25);
      m_direction = Direction.FORWARD;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
