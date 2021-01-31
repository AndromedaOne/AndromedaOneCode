/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;

public class MockDriveTrain extends DriveTrain {
  /**
   * Creates a new MockDriveTrain.
   */
  public MockDriveTrain() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
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

  public void moveUsingGyro(double forwardBackward, double rotation, boolean useDelay, boolean useSquaredInputs) {

  }

  public void moveUsingGyro(double forwardBackward, double rotation, boolean useDelay, boolean useSquaredInputs,
      double heading) {

  }

  @Override
  public void moveUsingGyro(double forwardBackward, double rotation, double heading) {
    // TODO Auto-generated method stub

  }

  @Override
  public Pose2d getPose() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    // TODO Auto-generated method stub

  }

  @Override
  public void resetOdometry(Pose2d pose) {
    // TODO Auto-generated method stub

  }
}
