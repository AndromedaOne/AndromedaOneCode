/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import com.revrobotics.CANSparkMax.IdleMode;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class SparkMaxDriveTrain extends RealDriveTrain {
  // public static SparkMaxController

  private final SparkMaxController m_frontLeft;
  private final SparkMaxController m_backLeft;
  private final SparkMaxController m_frontRight;
  private final SparkMaxController m_backRight;

  // motors on the Left side of the drive
  private final SpeedControllerGroup m_leftmotors;

  // motors on the right side of the drive
  private final SpeedControllerGroup m_rightmotors;

  private double ticksPerInch;

  public SparkMaxDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();

    m_frontLeft = new SparkMaxController(drivetrainConfig, "frontleft");
    m_backLeft = new SparkMaxController(drivetrainConfig, "backleft");
    m_frontRight = new SparkMaxController(drivetrainConfig, "frontright");
    m_backRight = new SparkMaxController(drivetrainConfig, "backright");

    // motors on the left side of the drive
    m_leftmotors = new SpeedControllerGroup(m_frontLeft, m_backLeft);

    // motors on the right side of the drive.
    m_rightmotors = new SpeedControllerGroup(m_frontRight, m_backRight);

    ticksPerInch = drivetrainConfig.getDouble("ticksPerInch");
  }

  @Override
  public double getRobotPositionInches() {
    double encoderPositionAvg = 0;
    int encoders = 0;
    if (m_frontLeft.hasEncoder()) {
      encoders++;
      encoderPositionAvg += m_frontLeft.getEncoderPositionTicks();
    }
    if (m_backLeft.hasEncoder()) {
      encoders++;
      encoderPositionAvg += m_backLeft.getEncoderPositionTicks();
    }
    if (m_frontRight.hasEncoder()) {
      encoders++;
      encoderPositionAvg += m_frontRight.getEncoderPositionTicks();
    }
    if (m_backRight.hasEncoder()) {
      encoders++;
      encoderPositionAvg += m_backRight.getEncoderPositionTicks();
    }
    if (encoders > 0) {
      encoderPositionAvg = encoderPositionAvg / (ticksPerInch * encoders);
    }
    return encoderPositionAvg;
  }

  @Override
  public double getRobotVelocityInches() {
    double encoderVelocityAvg = 0;
    int encoders = 0;
    if (m_frontLeft.hasEncoder()) {
      encoders++;
      encoderVelocityAvg += m_frontLeft.getEncoderVelocityTicks();
    }
    if (m_backLeft.hasEncoder()) {
      encoders++;
      encoderVelocityAvg += m_backLeft.getEncoderVelocityTicks();
    }
    if (m_frontRight.hasEncoder()) {
      encoders++;
      encoderVelocityAvg += m_frontRight.getEncoderVelocityTicks();
    }
    if (m_backRight.hasEncoder()) {
      encoders++;
      encoderVelocityAvg += m_backRight.getEncoderVelocityTicks();
    }
    if (encoders > 0) {
      encoderVelocityAvg = encoderVelocityAvg / (ticksPerInch * encoders);
    }
    return encoderVelocityAvg;
  }

  @Override
  protected SpeedControllerGroup getLeftSpeedControllerGroup() {
    return m_leftmotors;
  }

  @Override
  protected SpeedControllerGroup getRightSpeedControllerGroup() {
    return m_rightmotors;
  }

  @Override
  public void setCoast(boolean p) {
    System.out.println("coast set to " + p);
    if (p) {
      m_frontLeft.setIdleMode(IdleMode.kCoast);
      m_frontRight.setIdleMode(IdleMode.kCoast);
      m_backLeft.setIdleMode(IdleMode.kCoast);
      m_backRight.setIdleMode(IdleMode.kCoast);
    } else {
      m_frontLeft.setIdleMode(IdleMode.kBrake);
      m_frontRight.setIdleMode(IdleMode.kBrake);
      m_backLeft.setIdleMode(IdleMode.kBrake);
      m_backRight.setIdleMode(IdleMode.kBrake);

    }
  }
}