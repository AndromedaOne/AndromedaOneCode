// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.SubsystemInterface;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.utils.PoseEstimation4905;

/** Add your docs here. */
public interface DriveTrainBase extends SubsystemInterface {

  public abstract void init();

  public abstract void move(double fowardBackSpeed, double rotateAmount, boolean squaredInput);

  public abstract void move(double forwardBackward, double strafe, double rotation,
      boolean fieldRelative, boolean isOpenLoop);

  /**
   * This moves the robot and corrects for any rotation using the gyro
   * 
   * @param heading Setting a heading will allow you to set what angle the robot
   *                will correct to. This is useful in auto after the robot turns
   *                you can tell it to correct to the heading it should have turn
   *                to.
   */
  public abstract void moveUsingGyro(double forwardBackward, double rotation,
      boolean useSquaredInputs, double heading);

  public abstract void moveUsingGyroStrafe(double forwardBackward, double strafe, double rotation,
      boolean useSquaredInputs, double heading);

  /**
   * This moves the robot and corrects for any rotation using the gyro
   * 
   * @param heading Setting a heading will allow you to set what angle the robot
   *                will correct to. This is useful in auto after the robot turns
   *                you can tell it to correct to the heading it should have turn
   *                to.
   */
  public void stop();

  public abstract void enableParkingBrakes();

  public abstract void disableParkingBrakes();

  public abstract ParkingBrakeStates getParkingBrakeState();

  public abstract boolean hasParkingBrake();

  public abstract double getRobotPositionInches();

  public abstract double getRobotVelocityInches();

  public abstract Pose2d getPose();

  public abstract boolean resetOdometry(Pose2d pose);

  public abstract void setCoast(boolean value);

  public abstract void setDriveTrainMode(DriveTrainModeEnum mode);

  public abstract DriveTrainModeEnum getDriveTrainMode();

  public abstract void setToAngle(double angle);

  public abstract void setToZero();

  public abstract void enableAccelerationLimiting();

  public abstract void disableAccelerationLimiting();

  public abstract PoseEstimation4905.RegionsForPose getRegion();

  public abstract boolean isLeftSide();

  public abstract void configurePathPlanner();

  public abstract boolean isUnsafeZone();

}
