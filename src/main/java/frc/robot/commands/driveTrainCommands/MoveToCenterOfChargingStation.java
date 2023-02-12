// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class MoveToCenterOfChargingStation extends CommandBase {
  private DriveTrain m_driveTrain;
  private double m_distance = 0;
  private double m_maxOutput = 0;
  private double m_compassHeading = 0;
  private boolean m_driveForward = false;

  /** Creates a new MoveToCenterOfChargingStation. */
  public MoveToCenterOfChargingStation(DriveTrain driveTrain, double distance, double maxOutput,
      double compassHeading) {
    addRequirements(driveTrain);
    m_driveTrain = driveTrain;
    m_distance = distance;
    m_maxOutput = maxOutput;
    if (m_distance < 0) {
      m_driveForward = false;
      m_maxOutput = -m_maxOutput;
    } else {
      m_driveForward = true;
    }
    m_compassHeading = compassHeading;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_distance += m_driveTrain.getRobotPositionInches();
    Trace.getInstance().logCommandStart(this);
    Trace.getInstance().logCommandInfo(this, "target distance: " + m_distance);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveTrain.moveUsingGyro(m_maxOutput, 0, false, m_compassHeading);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    m_driveTrain.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (!m_driveForward && (m_driveTrain.getRobotPositionInches() <= m_distance)) {
      return true;
    } else if (m_driveForward && (m_driveTrain.getRobotPositionInches() >= m_distance)) {
      return true;
    }
    return false;
  }
}
