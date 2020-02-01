/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Config4905;

public abstract class RealDriveTrain extends DriveTrain {
  private int ticksPerInch;

  // the robot's main drive
  private DifferentialDrive m_drive;

  public RealDriveTrain() {

  }

  public void init() {
    m_drive = new DifferentialDrive(getLeftSpeedControllerGroup(), getRightSpeedControllerGroup());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Drives the robot using arcadeDrive
   * 
   * @param forwardBackSpeed Positive values go forward, negative goes backwards
   * @param rotateAmount     Per the right hand rule, positive goes
   *                         counter-clockwise and negative goes clockwise.
   */
  public void move(final double forwardBackSpeed, final double rotateAmount, final boolean squaredInput) {
    m_drive.arcadeDrive(forwardBackSpeed, -rotateAmount);
  }

  protected abstract SpeedControllerGroup getLeftSpeedControllerGroup();

  protected abstract SpeedControllerGroup getRightSpeedControllerGroup();
}