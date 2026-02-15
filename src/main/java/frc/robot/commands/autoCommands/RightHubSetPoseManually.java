// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autoCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class RightHubSetPoseManually extends Command {
  private DriveTrainBase m_drivetrain;

  public RightHubSetPoseManually() {
    m_drivetrain = Robot.getInstance().getSubsystemsContainer().getDriveTrain();
    addRequirements(m_drivetrain.getSubsystemBase());
  }

  @Override
  public void initialize() {
    double x = 3.676;
    double y = 3.856;
    Rotation2d rotation = Rotation2d.fromDegrees(180);
    m_drivetrain.resetOdometry(new Pose2d(x, y, rotation));
  }

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