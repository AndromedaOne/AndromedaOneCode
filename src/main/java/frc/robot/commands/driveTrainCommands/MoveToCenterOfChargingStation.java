// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class MoveToCenterOfChargingStation extends CommandBase {
  DriveTrain m_driveTrain;
  double m_distance = 0;
  double m_maxOutput = 0;

  /** Creates a new MoveToCenterOfChargingStation. */
  public MoveToCenterOfChargingStation(DriveTrain driveTrain, double distance, double maxOutput) {
    addRequirements(driveTrain);
    m_driveTrain = driveTrain;
    m_distance = distance;
    m_maxOutput = maxOutput;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_distance += m_driveTrain.getRobotPositionInches();
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveTrain.moveUsingGyro(m_maxOutput, 0, false,
        Robot.getInstance().getSensorsContainer().getGyro().getCompassHeading());
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
    if (m_driveTrain.getRobotPositionInches() >= m_distance) {
      return true;
    }
    return false;
  }
}
