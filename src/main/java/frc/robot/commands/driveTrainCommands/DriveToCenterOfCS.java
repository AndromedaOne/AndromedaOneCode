// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

public class DriveToCenterOfCS extends CommandBase {
  private DriveTrain m_driveTrain;
  private boolean m_driveForward = true;
  private double m_speed = 0;
  private boolean m_tippedUp = false;
  private double m_compassHeading = 0;
  private Gyro4905 m_gyro4905 = Robot.getInstance().getSensorsContainer().getGyro();
  private boolean m_done = false;

  /** Creates a new DriveToCenterOfCS. */
  public DriveToCenterOfCS(DriveTrain driveTrain, boolean driveForward, double speed,
      double compassHeading) {
    m_driveTrain = driveTrain;
    m_driveForward = driveForward;
    m_speed = Math.abs(speed);
    m_speed = m_driveForward ? m_speed : -m_speed;
    m_compassHeading = compassHeading;
    addRequirements(driveTrain.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!m_tippedUp) {
      if (m_gyro4905.getYAngle() >= 14) {
        m_tippedUp = true;
      } else {
        m_driveTrain.moveUsingGyro(m_speed, 0, false, m_compassHeading);
      }
    } else {
      if (m_gyro4905.getYAngle() <= 13) {
        m_done = true;
        m_driveTrain.stop();
      } else {
        m_driveTrain.moveUsingGyro(m_speed, 0, false, m_compassHeading);
      }
    }
    double tipped = m_tippedUp ? 1 : 0;
    Trace.getInstance().addTrace(true, getClass().getSimpleName(),
        new TracePair("Y angle", m_gyro4905.getYAngle()),
        new TracePair("Tipped, 0:false 1:true", tipped));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveTrain.stop();
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_done;
  }
}
