/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class DriveTrain extends SubsystemBase {
  /**
   * Creates a new DriveTrainBase.
   */
  public DriveTrain() {
  }

  public abstract void init();

  public abstract void move(double forwardBackSpeed, double rotateAmount, boolean squaredInput);

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

  public void stop() {
    move(0, 0, false);
  }

  public abstract double getRobotPositionInches();

  public abstract double getRobotVelocityInches();

  public abstract Pose2d getPose();

  public abstract DifferentialDriveWheelSpeeds getWheelSpeeds();

  public abstract void tankDriveVolts(double leftVolts, double rightVolts);

  public abstract void resetOdometry(Pose2d pose);

  public abstract void enableParkingBrakes();

  public abstract void disableParkingBrakes();

  public abstract void maintainParkingBrakeState();

  public abstract ParkingBrakeStates getParkingBrakeState();

  public abstract double getLeftRateMetersPerSecond();

  public abstract double getRightRateMetersPerSecond();

  public void setCoast(boolean p) {
    System.out.println("coast set to " + p);
  }

  public void periodic() {
    SmartDashboard.putNumber("robotPositionInches", getRobotPositionInches());
    SmartDashboard.putNumber("Left Wheel Speed", getLeftRateMetersPerSecond());
    SmartDashboard.putNumber("Right Wheel Speed", getRightRateMetersPerSecond());
    SmartDashboard.putString("Parking Brake State", getParkingBrakeState().name());
  }
}
