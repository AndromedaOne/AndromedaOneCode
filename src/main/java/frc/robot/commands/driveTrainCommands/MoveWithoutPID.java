// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drivetrain.tankDriveTrain.TankDriveTrain;
import frc.robot.telemetries.Trace;

/*Move robot without using a PID loop. Use this to move the robot over uneven ground.
 * The robot will roll past the distance due to momentum.
 */
public class MoveWithoutPID extends Command {
  private TankDriveTrain m_driveTrain;
  private double m_distance = 0;
  private double m_speed = 0;
  private double m_compassHeading = 0;
  private boolean m_driveForward = false;

  /** Creates a new MoveToCenterOfChargingStation. */
  public MoveWithoutPID(TankDriveTrain driveTrain, double distance, double speed,
      double compassHeading) {
    addRequirements(driveTrain.getSubsystemBase());
    m_driveTrain = driveTrain;
    m_distance = distance;
    m_speed = speed;
    if (m_distance < 0) {
      m_driveForward = false;
      m_speed = -m_speed;
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
    m_driveTrain.moveUsingGyro(m_speed, 0, false, m_compassHeading);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    m_driveTrain.stop();
    Trace.getInstance().logCommandInfo(this,
        "ending position: " + m_driveTrain.getRobotPositionInches());
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
