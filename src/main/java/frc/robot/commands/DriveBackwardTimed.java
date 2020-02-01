/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

/**
 * Add your docs here.
 */
public class DriveBackwardTimed extends CommandBase {
  /**
   * Add your docs here.
   */
  private double m_timeout;
  private Timer m_timer;
  private DriveTrain m_driveTrain;

  public DriveBackwardTimed(double timeout, DriveTrain driveTrain) {
    m_timeout = timeout;
    m_timer = new Timer();
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    m_driveTrain = driveTrain;
  }

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    m_timer.reset();
    m_timer.start();

  }

  @Override
  public void execute() {
    // TODO Auto-generated method stub
    m_driveTrain.move(-0.3, 0, false);
  }

  @Override
  public boolean isFinished() {

    return m_timer.hasPeriodPassed(m_timeout);
  }
}
