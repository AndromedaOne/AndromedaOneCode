// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmRotate;

public class MockSamArmRotate extends SamArmRotateBase {
  /** Creates a new MockSamArmRotate. */
  public MockSamArmRotate() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void rotate(double speed) {
  }

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public boolean getInitialized() {
    return true;
  }

  @Override
  public void setInitialized() {
  }

  @Override
  public void engageArmBrake() {

  }

  @Override
  public void disengageArmBrake() {

  }

  public ArmAngleBrakeState getState() {
    return ArmAngleBrakeState.ENGAGEARMBRAKE;
  }
}
