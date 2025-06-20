
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class SwerveDriveSetWheelsToAngle extends Command {
  DriveTrainBase m_driveTrainBase;
  int m_count = 0;
  DoubleSupplier m_angle = () -> 0;
  boolean m_useSmartDashboard = false;

  /**
   * Creates a new SwerveDriveSetWheelsToAngle. The angle passed in is counter
   * clockwise positive.
   */
  public SwerveDriveSetWheelsToAngle(DriveTrainBase drivetrain, DoubleSupplier angle,
      boolean useSmartDashboard) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drivetrain.getSubsystemBase());
    m_driveTrainBase = drivetrain;
    m_angle = angle;
    m_useSmartDashboard = useSmartDashboard;
  }

  public SwerveDriveSetWheelsToAngle(DriveTrainBase drivetrain, double angle,
      boolean useSmartDashboard) {
    // Use addRequirements() here to declare subsystem dependencies.
    this(drivetrain, () -> angle, useSmartDashboard);
  }

  public SwerveDriveSetWheelsToAngle(DriveTrainBase drivetrain, DoubleSupplier angle) {
    this(drivetrain, angle, false);
  }

  public SwerveDriveSetWheelsToAngle(DriveTrainBase drivetrain, double angle) {
    this(drivetrain, angle, false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_count = 0;
    if (m_useSmartDashboard) {
      m_driveTrainBase.setToAngle(SmartDashboard.getNumber("Set swerve drive angle for test", 0));
    } else {
      m_driveTrainBase.setToAngle(m_angle.getAsDouble());
    }
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
    if (m_count >= 25) {
      return true;
    }
    return false;
  }
}
