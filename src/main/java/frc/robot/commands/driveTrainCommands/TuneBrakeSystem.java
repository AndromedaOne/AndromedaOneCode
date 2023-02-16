// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class TuneBrakeSystem extends CommandBase {
  /** Creates a new TuneBrakeSystem. */
  DriveTrain m_driveTrain;

  public TuneBrakeSystem(DriveTrain driveTrain) {
    m_driveTrain = driveTrain;

    // throw on the dashboard a default value
    SmartDashboard.putNumber("Left Brake Speed", 0.0);
    SmartDashboard.putNumber("Right Brake Speed", 0.0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);

    double leftSpeed = SmartDashboard.getNumber("Left Brake Speed", 0.0);
    double rightSpeed = SmartDashboard.getNumber("Right Brake Speed", 0.0);
    m_driveTrain.setParkingBrakes(leftSpeed, rightSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
