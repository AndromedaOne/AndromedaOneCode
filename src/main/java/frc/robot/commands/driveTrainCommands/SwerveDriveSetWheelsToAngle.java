
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class SwerveDriveSetWheelsToAngle extends Command {
  DriveTrainBase m_driveTrainBase;
  int m_count = 0;
  double m_angle = 0;

  /** Creates a new SwerveDriveSetWheelsToAngle. */
  public SwerveDriveSetWheelsToAngle(DriveTrainBase drivetrain, double angle) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain.getSubsystemBase());
    m_driveTrainBase = drivetrain;
    m_angle = angle;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_driveTrainBase.setToAngle(m_angle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ++m_count;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrainBase.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_count >= 2) {
      return true;
    }
    return false;
  }
}
