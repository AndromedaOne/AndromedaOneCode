/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain.tankDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.utils.PoseEstimation4905;

public class MockTankDriveTrain implements TankDriveTrain {
  /**
   * Creates a new MockDriveTrain
   */
  public MockTankDriveTrain() {
  }

  @Override
  public void init() {
  }

  public void move(double forwardBackSpeed, double rotateAmount, boolean squaredInput) {
  }

  @Override
  public double getRobotPositionInches() {
    return 0;
  }

  @Override
  public double getRobotVelocityInches() {
    return 0;
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double heading) {
  }

  @Override
  public Pose2d getPose() {
    return new Pose2d(0, 0, new Rotation2d(0));
  }

  @Override
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(0, 0);
  }

  @Override
  public void tankDriveVolts(double leftVolts, double rightVolts) {

  }

  @Override
  public ParkingBrakeStates getParkingBrakeState() {
    return ParkingBrakeStates.BRAKESON;
  }

  @Override
  public boolean resetOdometry(Pose2d pose) {
    return true;
  }

  @Override
  public void enableParkingBrakes() {

  }

  @Override
  public void disableParkingBrakes() {

  }

  @Override
  public double getLeftRateMetersPerSecond() {
    return 0;
  }

  @Override
  public double getRightRateMetersPerSecond() {
    return 0;
  }

  @Override
  public boolean hasParkingBrake() {
    return false;
  }

  @Override
  public void stop() {
  }

  @Override
  public void setCoast(boolean value) {
  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    return DriveTrainModeEnum.SLOW;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void move(double forwardBackSpeed, double strafe, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
  }

  @Override
  public void setToAngle(double angle) {
  }

  public void moveUsingGyroStrafe(double forwardBackward, double angle, double rotation,
      boolean useSquaredInputs, double compassHeading) {
  }

  @Override
  public void setToZero() {
  }

  @Override
  public void enableAccelerationLimiting() {

  }

  @Override
  public void disableAccelerationLimiting() {

  }

  public void resetOdometryForCalibration() {

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

  @Override
  public void setVelocityToZero() {
  }

  @Override
  public Pose2d currentPose2d() {
    return null;
  }

}
