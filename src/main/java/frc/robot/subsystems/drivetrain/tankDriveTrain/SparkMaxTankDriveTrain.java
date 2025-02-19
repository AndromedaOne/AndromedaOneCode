/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain.tankDriveTrain;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.telemetries.Trace;
import frc.robot.utils.PoseEstimation4905;

@SuppressWarnings("removal")
public class SparkMaxTankDriveTrain extends RealTankDriveTrain {
  // public static SparkMaxController

  private final SparkMaxController m_frontLeft;
  private final SparkMaxController m_backLeft;
  private final SparkMaxController m_frontRight;
  private final SparkMaxController m_backRight;

  // motors on the Left side of the drive
  private final edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup m_leftmotors;

  // motors on the right side of the drive
  private final edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup m_rightmotors;

  private double ticksPerInch;
  public static final double metersPerInch = 0.0254;

  public SparkMaxTankDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();

    m_frontLeft = new SparkMaxController(drivetrainConfig, "frontleft", false, false);
    m_backLeft = new SparkMaxController(drivetrainConfig, "backleft", false, false);
    m_frontRight = new SparkMaxController(drivetrainConfig, "frontright", false, false);
    m_backRight = new SparkMaxController(drivetrainConfig, "backright", false, false);

    // motors on the left side of the drive
    m_leftmotors = new edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup(
        m_frontLeft.getMotorController(), m_backLeft.getMotorController());

    // motors on the right side of the drive.
    m_rightmotors = new edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup(
        m_frontRight.getMotorController(), m_backRight.getMotorController());

    ticksPerInch = drivetrainConfig.getDouble("ticksPerInch");
  }

  @Override
  public double getRobotPositionInches() {
    double encoderPositionAvg = 0;
    int encoders = 0;
    if (m_frontLeft.hasAbsoluteEncoder()) {
      encoders++;
      encoderPositionAvg += m_frontLeft.getBuiltInEncoderPositionTicks();
      SmartDashboard.putNumber("FrontLeftDriveTrain", m_frontLeft.getBuiltInEncoderPositionTicks());
    }
    if (m_backLeft.hasAbsoluteEncoder()) {
      encoders++;
      encoderPositionAvg += m_backLeft.getBuiltInEncoderPositionTicks();
      SmartDashboard.putNumber("BackLeftDriveTrain", m_backLeft.getBuiltInEncoderPositionTicks());
    }
    if (m_frontRight.hasAbsoluteEncoder()) {
      encoders++;
      encoderPositionAvg += m_frontRight.getBuiltInEncoderPositionTicks();
      SmartDashboard.putNumber("FrontRightDriveTrain",
          m_frontRight.getBuiltInEncoderPositionTicks());
    }
    if (m_backRight.hasAbsoluteEncoder()) {
      encoders++;
      encoderPositionAvg += m_backRight.getBuiltInEncoderPositionTicks();
      SmartDashboard.putNumber("BackRightDriveTrain", m_backRight.getBuiltInEncoderPositionTicks());
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
    if (m_frontLeft.hasAbsoluteEncoder()) {
      encoders++;
      encoderVelocityAvg += m_frontLeft.getBuiltInEncoderVelocityTicks();
    }
    if (m_backLeft.hasAbsoluteEncoder()) {
      encoders++;
      encoderVelocityAvg += m_backLeft.getBuiltInEncoderVelocityTicks();
    }
    if (m_frontRight.hasAbsoluteEncoder()) {
      encoders++;
      encoderVelocityAvg += m_frontRight.getBuiltInEncoderVelocityTicks();
    }
    if (m_backRight.hasAbsoluteEncoder()) {
      encoders++;
      encoderVelocityAvg += m_backRight.getBuiltInEncoderVelocityTicks();
    }
    if (encoders > 0) {
      encoderVelocityAvg = encoderVelocityAvg / (ticksPerInch * encoders);
    }
    return encoderVelocityAvg;
  }

  protected edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup getLeftSpeedControllerGroup() {
    return m_leftmotors;
  }

  @Override
  protected edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup getRightSpeedControllerGroup() {
    return m_rightmotors;
  }

  @Override
  public void setCoast(boolean value) {
    Trace.getInstance().logInfo("coast set to " + value);
    if (value) {
      m_frontLeft.setCoastMode();
      m_frontRight.setCoastMode();
      m_backLeft.setCoastMode();
      m_backRight.setCoastMode();
    } else {
      m_frontLeft.setBrakeMode();
      m_frontRight.setBrakeMode();
      m_backLeft.setBrakeMode();
      m_backRight.setBrakeMode();

    }
  }

  public double getLeftRateMetersPerSecond() {
    return ticksPerMinuteToMetersPerSecond(
        (m_backLeft.getBuiltInEncoderVelocityTicks() + m_frontLeft.getBuiltInEncoderVelocityTicks())
            * 0.5);
  }

  @Override
  public double getRightRateMetersPerSecond() {
    return ticksPerMinuteToMetersPerSecond((m_backRight.getBuiltInEncoderVelocityTicks()
        + m_frontRight.getBuiltInEncoderVelocityTicks()) * 0.5);
  }

  @Override
  protected double getLeftSideMeters() {
    double averageTicks = (m_backLeft.getBuiltInEncoderPositionTicks()
        + m_frontLeft.getBuiltInEncoderPositionTicks()) * 0.5;
    double averageMeters = ticksToMeters(averageTicks);
    return averageMeters;
  }

  @Override
  protected double getRightsSideMeters() {
    double averageTicks = (m_backRight.getBuiltInEncoderPositionTicks()
        + m_frontRight.getBuiltInEncoderPositionTicks()) * 0.5;
    double averageMeters = ticksToMeters(averageTicks);
    return averageMeters;
  }

  private double ticksToMeters(double ticks) {
    return ticks * (1.0 / ticksPerInch) * metersPerInch;
  }

  private double ticksPerMinuteToMetersPerSecond(double ticksPerMinute) {
    return ticksToMeters(ticksPerMinute * (1.0 / 60));
  }

  @Override
  protected void resetEncoders() {
    m_backLeft.resetEncoder();
    m_frontLeft.resetEncoder();
    m_backRight.resetEncoder();
    m_frontRight.resetEncoder();
  }

  @Override
  public void move(double forwardBackSpeed, double strafe, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
  }

  @Override
  public void enableAccelerationLimiting() {

  }

  @Override
  public void disableAccelerationLimiting() {

  }

  @Override
  public void setToAngle(double angle) {
  }

  @Override
  public PoseEstimation4905.RegionsForPose getRegion() {
    return PoseEstimation4905.RegionsForPose.UNKNOWN;
  }

  @Override
  public boolean isLeftSide() {
    return false;
  }

  @Override
  public void configurePathPlanner() {
  }

  @Override
  public boolean isUnsafeZone() {
    return false;
  }

}