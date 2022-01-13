/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import com.typesafe.config.Config;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;
import frc.robot.utils.AngleConversionUtils;

public abstract class RealDriveTrain extends DriveTrain {
  // Gyro variables
  private Gyro4905 gyro;
  private double kProportion = 0.0;
  // the robot's main drive
  private DifferentialDrive m_drive;
  private DifferentialDriveOdometry m_odometry;
  private int m_rightSideInvertedMultiplier = -1;
  private boolean m_invertFowardAndBack = false;

  public RealDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    gyro = Robot.getInstance().getSensorsContainer().getGyro();
    kProportion = drivetrainConfig.getDouble("gyrocorrect.kproportion");
    System.out.println("RealDriveTrain kProportion = " + kProportion);
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    super.periodic();
    boolean usingOdometry = false;
    if (usingOdometry) {
      double leftMeters = getLeftSideMeters();
      double rightMeters = getRightsSideMeters();
      double angle = Math.toRadians(gyro.getAngle());
      m_odometry.update(new Rotation2d(angle), leftMeters, rightMeters);
      SmartDashboard.putNumber("OdometryX", m_odometry.getPoseMeters().getX());
      SmartDashboard.putNumber("OdometryY", m_odometry.getPoseMeters().getY());
      SmartDashboard.putNumber("OdometryRotation",
          m_odometry.getPoseMeters().getRotation().getDegrees());
      SmartDashboard.putNumber("OdometryAngle", angle);
      SmartDashboard.putNumber("Odometry Romi Left Motor Position", leftMeters);
      SmartDashboard.putNumber("Odometry Romi Right Motor Position", rightMeters);
      SmartDashboard.putNumber("OdometryLeftSpeed", getLeftRateMetersPerSecond());
      SmartDashboard.putNumber("OdometryRightSpeed", getRightRateMetersPerSecond());
    }
  }

  public void init() {
    resetEncoders();
    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
    m_drive = new DifferentialDrive(getLeftSpeedControllerGroup(), getRightSpeedControllerGroup());
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    if (drivetrainConfig.hasPath("rightSideInverted")) {
      // TODO DifferentialDrive does not support setRight/LeftSideInverted
      boolean rightSideInverted = drivetrainConfig.getBoolean("rightSideInverted");
      // m_drive.setRightSideInverted(rightSideInverted);
      m_rightSideInvertedMultiplier = rightSideInverted ? -1 : 1;
    }
    if (drivetrainConfig.hasPath("InvertFowardAndBack")) {
      m_invertFowardAndBack = true;
    }
  }

  /**
   * This moves the robot and corrects for any rotation using the gyro
   * 
   * @param compassHeading Setting a heading will allow you to set what angle the
   *                       robot will correct to. This is useful in auto after the
   *                       robot turns you can tell it to correct to the heading
   *                       it should have turn to.
   */
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double compassHeading) {

    if (rotation == 0.0) {
      double robotDeltaAngle = AngleConversionUtils
          .calculateMinimalCompassHeadingDifference(gyro.getCompassHeading(), compassHeading);
      rotation = -robotDeltaAngle * kProportion;
      Trace.getInstance().addTrace(true, "MoveUsingGyro",
          new TracePair<Double>("CompassHeading", compassHeading),
          new TracePair<>("GyroCompassHeading", gyro.getCompassHeading()),
          new TracePair<>("robotDeltaAngle", robotDeltaAngle),
          new TracePair<>("rotation", rotation));
    }
    move(forwardBackward, rotation, useSquaredInputs);
  }

  /**
   * Drives the robot using arcadeDrive
   * 
   * @param forwardBackSpeed Positive values go forward, negative goes backwards
   * @param rotateAmount     Positive rotates clockwise, negative counter
   *                         clockwise
   */
  public void move(double forwardBackSpeed, double rotateAmount, boolean squaredInput) {
    if (m_invertFowardAndBack) {
      forwardBackSpeed = -forwardBackSpeed;
    }
    m_drive.arcadeDrive(forwardBackSpeed, rotateAmount, squaredInput);
  }

  protected abstract MotorControllerGroup getLeftSpeedControllerGroup();

  protected abstract MotorControllerGroup getRightSpeedControllerGroup();

  protected abstract double getLeftRateMetersPerSecond();

  protected abstract double getRightRateMetersPerSecond();

  @Override
  public Pose2d getPose() {
    Pose2d pose = m_odometry.getPoseMeters();
    return pose;
  }

  @Override
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    DifferentialDriveWheelSpeeds wheelSpeeds = new DifferentialDriveWheelSpeeds(
        getLeftRateMetersPerSecond(), getRightRateMetersPerSecond());
    return wheelSpeeds;
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    double angle = gyro.getAngle();
    SmartDashboard.putNumber("ResetOdometryAngle", angle);
    m_odometry.resetPosition(pose, new Rotation2d(angle));
  }

  @Override
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    double averageSpeed = leftVolts + rightVolts;
    if (averageSpeed < 0) {
      Robot.getInstance().getSubsystemsContainer().getLEDs("LEDStringOne").setRGB(1.0, 1.0, 1.0);
    } else {
      Robot.getInstance().getSubsystemsContainer().getLEDs("LEDStringOne").setRGB(0, 0, 1.0);
    }
    getLeftSpeedControllerGroup().setVoltage(leftVolts);
    getRightSpeedControllerGroup().setVoltage(m_rightSideInvertedMultiplier * rightVolts);
    m_drive.feed();
  }

  protected abstract void resetEncoders();

  protected abstract double getLeftSideMeters();

  protected abstract double getRightsSideMeters();
}