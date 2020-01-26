/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealDriveTrain extends DriveTrain {
  // public static SparkMaxController

  // motors on the Left side of the drive
  private final SpeedControllerGroup m_leftmotors = new SpeedControllerGroup(new SparkMaxController("frontleft"),
      new SparkMaxController("backleft"));

  // motors on the right side of the drive
  private final SpeedControllerGroup m_rightmotors = new SpeedControllerGroup(new SparkMaxController("frontright"),
      new SparkMaxController("backright"));

  private final SparkMaxController m_frontLeft = new SparkMaxController("frontleft");
  private final SparkMaxController m_backLeft = new SparkMaxController("backleft");
  private final SparkMaxController m_frontRight = new SparkMaxController("frontright");
  private final SparkMaxController m_backRight = new SparkMaxController("backright");

  private int ticksPerInch = Config4905.getConfig4905().getDrivetrainConfig().getInt("drivetrain.ticksPerInch");

  // the robot's main drive
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftmotors, m_rightmotors);

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