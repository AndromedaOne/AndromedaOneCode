/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drivetrain;

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

  public void moveUsingGyroForHumanDriver(double forwardBackward, double rotation, boolean useDelay,
      boolean useSquaredInputs) {

  }

  @Override
  public void moveUsingGyroForAutomated(double forwardBackward, double rotation, double heading) {

  }

  public void resetCurrentDelay() {

  }
}
