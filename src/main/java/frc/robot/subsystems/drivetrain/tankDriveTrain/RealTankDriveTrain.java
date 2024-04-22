/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain.tankDriveTrain;

import com.typesafe.config.Config;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.kinematics.*;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.HitecHS322HDpositionalServoMotor;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.drivetrain.DriveTrainMode;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;
import frc.robot.utils.AngleConversionUtils;

@SuppressWarnings("removal")
public abstract class RealTankDriveTrain extends SubsystemBase implements TankDriveTrain {
  // Gyro variables
  private Gyro4905 gyro;
  private double kProportion = 0.0;
  // the robot's main drive
  private DifferentialDrive m_drive;
  private DifferentialDriveOdometry m_odometry;
  private int m_rightSideInvertedMultiplier = -1;
  private boolean m_invertFowardAndBack = false;
  private HitecHS322HDpositionalServoMotor m_leftServoMotor;
  private HitecHS322HDpositionalServoMotor m_rightServoMotor;
  private ParkingBrakeStates m_parkingBrakeStates = ParkingBrakeStates.UNKNOWN;
  private double m_leftBrakeEngagedValue = 0.0;
  private double m_rightBrakeEngagedValue = 0.0;
  private double m_leftBrakeDisengagedValue = 0.0;
  private double m_rightBrakeDisengagedValue = 0.0;
  private boolean m_hasParkingBrake = false;
  private final double m_maxSpeedToEngageBrake = 0.85;
  private DriveTrainMode m_driveTrainMode = new DriveTrainMode();

  public RealTankDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    gyro = Robot.getInstance().getSensorsContainer().getGyro();
    kProportion = drivetrainConfig.getDouble("gyrocorrect.kproportion");
    System.out.println("RealDriveTrain kProportion = " + kProportion);
    if (drivetrainConfig.hasPath("parkingbrake")) {
      m_leftServoMotor = new HitecHS322HDpositionalServoMotor(drivetrainConfig,
          "parkingbrake.leftbrakeservomotor");
      m_leftBrakeEngagedValue = drivetrainConfig.getDouble("parkingbrake.leftbrakeengaged");
      m_leftBrakeDisengagedValue = drivetrainConfig.getDouble("parkingbrake.leftbrakedisengage");
      m_rightServoMotor = new HitecHS322HDpositionalServoMotor(drivetrainConfig,
          "parkingbrake.rightbrakeservomotor");
      m_rightBrakeEngagedValue = drivetrainConfig.getDouble("parkingbrake.rightbrakeengaged");
      m_rightBrakeDisengagedValue = drivetrainConfig.getDouble("parkingbrake.rightbrakedisengage");
      m_hasParkingBrake = true;
    } else {
      m_hasParkingBrake = false;
      m_parkingBrakeStates = ParkingBrakeStates.BRAKESOFF;
    }
    if (Config4905.getConfig4905().isShowBot() || Config4905.getConfig4905().isTopGun()) {
      setDriveTrainMode(DriveTrainModeEnum.SLOW);
    }
  }

  public void init() {
    resetEncoders();
    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0), 0, 0);
    // DifferentialDrive no longer allows us to invert the right side. have to
    // invert the SpeedController on the right side instead
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    if (drivetrainConfig.hasPath("rightSideInverted")) {
      boolean rightSideInverted = drivetrainConfig.getBoolean("rightSideInverted");
      getRightSpeedControllerGroup().setInverted(rightSideInverted);
      m_rightSideInvertedMultiplier = rightSideInverted ? -1 : 1;
    }
    m_drive = new DifferentialDrive(getLeftSpeedControllerGroup(), getRightSpeedControllerGroup());
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
          new TracePair("CompassHeading", compassHeading),
          new TracePair("GyroCompassHeading", gyro.getCompassHeading()),
          new TracePair("robotDeltaAngle", robotDeltaAngle), new TracePair("rotation", rotation),
          new TracePair("ForwardBackward", forwardBackward));
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
    if (getParkingBrakeState() == ParkingBrakeStates.BRAKESOFF) {
      m_drive.arcadeDrive(forwardBackSpeed, -rotateAmount, squaredInput);
    } else {
      m_drive.arcadeDrive(0, 0);
    }
  }

  protected abstract edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup getLeftSpeedControllerGroup();

  protected abstract edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup getRightSpeedControllerGroup();

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
    m_odometry.resetPosition(new Rotation2d(angle), 0, 0, pose);
  }

  @Override
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    getLeftSpeedControllerGroup().setVoltage(leftVolts);
    getRightSpeedControllerGroup().setVoltage(m_rightSideInvertedMultiplier * rightVolts);
    m_drive.feed();
  }

  @Override
  public void enableParkingBrakes() {
    if (!m_hasParkingBrake) {
      return;
    }
    if (!((Math.abs(getLeftRateMetersPerSecond()) >= m_maxSpeedToEngageBrake)
        || (Math.abs(getRightRateMetersPerSecond()) >= m_maxSpeedToEngageBrake))) {
      m_leftServoMotor.set(m_leftBrakeEngagedValue);
      m_rightServoMotor.set(m_rightBrakeEngagedValue);
      m_parkingBrakeStates = ParkingBrakeStates.BRAKESON;
    } else {
      System.out.println("Moving too fast! Please slow down to engage brakes!");
    }
  }

  public void moveUsingGyroStrafe(double forwardBackward, double angle, double rotation,
      boolean useSquaredInputs, double compassHeading) {
  }

  @Override
  public void disableParkingBrakes() {
    if (!m_hasParkingBrake) {
      return;
    }
    m_leftServoMotor.set(m_leftBrakeDisengagedValue);
    m_rightServoMotor.set(m_rightBrakeDisengagedValue);
    m_parkingBrakeStates = ParkingBrakeStates.BRAKESOFF;
  }

  @Override
  public ParkingBrakeStates getParkingBrakeState() {
    return m_parkingBrakeStates;
  }

  @Override
  public boolean hasParkingBrake() {
    return m_hasParkingBrake;
  }

  protected abstract void resetEncoders();

  protected abstract double getLeftSideMeters();

  protected abstract double getRightsSideMeters();

  @Override
  public void stop() {
    move(0, 0, false);
  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
    m_driveTrainMode.setDriveTrainMode(mode);
  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    return m_driveTrainMode.getDriveTrainMode();
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return (this);
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  @Override
  public void setToAngle(double angle) {
  }

  @Override
  public void setToZero() {
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("robotPositionInches", getRobotPositionInches());
    SmartDashboard.putNumber("Left Wheel Speed", getLeftRateMetersPerSecond());
    SmartDashboard.putNumber("Right Wheel Speed", getRightRateMetersPerSecond());
    SmartDashboard.putString("Parking Brake State", getParkingBrakeState().name());
  }
}