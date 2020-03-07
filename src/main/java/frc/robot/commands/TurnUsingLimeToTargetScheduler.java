/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.pidcommands.TurnUsingLimeToTarget;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;

public class TurnUsingLimeToTargetScheduler extends CommandBase {
  // After 1 second we want to stop looking for the target
  private final double m_timeout = 1.0;
  private Timer m_timer;
  private LimeLightCameraBase m_limelight;
  private boolean isDone;
  TurnUsingLimeToTarget turnUsingLimeToTarget;

  /**
   * Schedules the TTF Command.
   * 
   * @param timeout The actual timeout value.
   * @param sensor  Double supplier.
   * 
   */
  public TurnUsingLimeToTargetScheduler(LimeLightCameraBase limeLightCameraBase) {
    m_timer = new Timer();
    m_limelight = limeLightCameraBase;
    isDone = false;
    turnUsingLimeToTarget = new TurnUsingLimeToTarget(limeLightCameraBase::horizontalDegreesToTarget);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
    isDone = false;
    m_limelight.enableLED();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_limelight.targetLock()) {
      CommandScheduler.getInstance().schedule(turnUsingLimeToTarget);
      isDone = true;
    } else if (m_timer.hasPeriodPassed(m_timeout)) {
      m_limelight.disableLED();
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
