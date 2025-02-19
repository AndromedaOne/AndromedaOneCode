package frc.robot.subsystems.drivetrain.swerveDriveTrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.subsystems.drivetrain.DriveTrainMode.DriveTrainModeEnum;
import frc.robot.subsystems.drivetrain.ParkingBrakeStates;
import frc.robot.utils.PoseEstimation4905;
import frc.robot.utils.PoseEstimation4905.RegionsForPose;

public class MockSwerveDriveTrain implements DriveTrainBase {

  @Override
  public Pose2d getPose() {
    return null;
  }

  @Override
  public boolean resetOdometry(Pose2d pose) {
    return true;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void init() {
  }

  @Override
  public void move(double fowardBackSpeed, double rotateAmount, boolean squaredInput) {
  }

  @Override
  public void move(double forwardBackSpeed, double strafe, double rotation, boolean fieldRelative,
      boolean isOpenLoop) {
  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, boolean useSquaredInputs,
      double heading) {
  }

  @Override
  public void stop() {
  }

  @Override
  public void enableParkingBrakes() {
  }

  @Override
  public void disableParkingBrakes() {
  }

  @Override
  public ParkingBrakeStates getParkingBrakeState() {
    return null;
  }

  @Override
  public boolean hasParkingBrake() {
    return false;
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
  public void setCoast(boolean value) {
  }

  @Override
  public void setDriveTrainMode(DriveTrainModeEnum mode) {
  }

  @Override
  public DriveTrainModeEnum getDriveTrainMode() {
    return null;
  }

  @Override
  public void setToAngle(double angle) {
  }

  @Override
  public void setToZero() {
  };

  @Override
  public void enableAccelerationLimiting() {

  }

  @Override
  public void disableAccelerationLimiting() {

  }

  public void moveUsingGyroStrafe(double forwardBackward, double angle, double rotation,
      boolean useSquaredInputs, double compassHeading) {
  }

  @Override
  public PoseEstimation4905.RegionsForPose getRegion() {
    return RegionsForPose.UNKNOWN;
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