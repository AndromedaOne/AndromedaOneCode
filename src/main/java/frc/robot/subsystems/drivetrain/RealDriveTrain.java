/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
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

  // the robot's main drive
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftmotors, m_rightmotors);

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void move(final double forwardBackSpeed, final double rotateAmount, final boolean squaredInput) {
    m_drive.arcadeDrive(forwardBackSpeed, rotateAmount);
  }

  @Override
  public double getEncoderPosition() {
    double encoderPositionAvg = (m_frontLeft.getEncoderPositionInches() + m_backLeft.getEncoderPositionInches()
        + m_frontRight.getEncoderPositionInches() + m_backRight.getEncoderPositionInches()) / 4;
    return encoderPositionAvg;
  }

  @Override
  public double getEncoderVelocity() {
    double encoderVelocityAvg = (m_frontLeft.getEncoderVelocityInches() + m_backLeft.getEncoderVelocityInches()
        + m_frontRight.getEncoderVelocityInches() + m_backRight.getEncoderVelocityInches()) / 4;
    return encoderVelocityAvg;
  }
}