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
import frc.robot.actuators.SparkMaxController;

public class RealDriveTrain extends DriveTrain {
  // public static SparkMaxController

  private final SparkMaxController m_frontLeft;
  private final SparkMaxController m_backLeft;
  private final SparkMaxController m_frontRight;
  private final SparkMaxController m_backRight;

  // motors on the Left side of the drive
  private final SpeedControllerGroup m_leftmotors;

  // motors on the right side of the drive
  private final SpeedControllerGroup m_rightmotors;

  private int ticksPerInch;

  // the robot's main drive
  private final DifferentialDrive m_drive;

  public RealDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();

    m_frontLeft = new SparkMaxController(drivetrainConfig, "frontleft");
    m_backLeft = new SparkMaxController(drivetrainConfig, "backleft");
    m_frontRight = new SparkMaxController(drivetrainConfig, "frontright");
    m_backRight = new SparkMaxController(drivetrainConfig, "backright");

    // motors on the left side of the drive
    m_leftmotors = new SpeedControllerGroup(m_frontLeft, m_backLeft);

    // motors on the right side of the drive.
    m_rightmotors = new SpeedControllerGroup(m_frontRight, m_backRight);

    m_drive = new DifferentialDrive(m_leftmotors, m_rightmotors);

    ticksPerInch = drivetrainConfig.getInt("drivetrain.ticksPerInch");

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

  @Override
  public double getEncoderPositionInches() {
    double encoderPositionAvg = (m_frontLeft.getEncoderPositionTicks() + m_backLeft.getEncoderPositionTicks()
        + m_frontRight.getEncoderPositionTicks() + m_backRight.getEncoderPositionTicks()) / 4 * ticksPerInch;
    return encoderPositionAvg;
  }

  @Override
  public double getEncoderVelocityInches() {
    double encoderVelocityAvg = (m_frontLeft.getEncoderVelocityTicks() + m_backLeft.getEncoderVelocityTicks()
        + m_frontRight.getEncoderVelocityTicks() + m_backRight.getEncoderVelocityTicks()) / 4 * ticksPerInch;
    return encoderVelocityAvg;
  }
}