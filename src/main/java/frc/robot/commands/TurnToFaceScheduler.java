/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;

public class TurnToFaceScheduler extends CommandBase {
  private double m_timeout;
  private Timer m_timer;
  private LimeLightCameraBase m_limelight;
  private DoubleSupplier m_sensor;
  private boolean isDone;

  /**
   * Schedules the TTF Command.
   * 
   * @param timeout The actual timeout value.
   * @param sensor  Double supplier.
   * 
   */
  public TurnToFaceScheduler(double timeout, DoubleSupplier sensor) {
    m_timeout = timeout;
    m_timer = new Timer();
    m_sensor = sensor;
    isDone = false;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_limelight.targetLock()) {
      CommandScheduler.getInstance().schedule(new TurnToFaceCommand(m_sensor));
      isDone = true;
    } else if (m_timer.hasPeriodPassed(m_timeout)) {
      isDone = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isDone;
  }
}
