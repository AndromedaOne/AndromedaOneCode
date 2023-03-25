// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

public class MockSamArmExtRet extends SamArmExtRetBase {
  /** Creates a new MockSamArmExtension. */
  public MockSamArmExtRet() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void extendRetract(double speed) {
  }

  @Override
  public double getPosition() {
    return 0;
  }

  @Override
  public void setZeroOffset() {
  }

  @Override
  public RetractLimitSwitchState getRetractLimitSwitchState() {
    return RetractLimitSwitchState.CLOSED;
  }

  @Override
  public void retractArmInitialize() {

  }

  @Override
  public boolean isInitialized() {
    return false;
  }

  @Override
  public void setInitialized() {
  }

  @Override
  public ExtensionBrakeStates getExtensionBrakeStates() {
    return ExtensionBrakeStates.BRAKEOPEN;
  }

  @Override
  public void engageArmBrake() {
  }

  @Override
  public void disengageArmBrake() {
  }

  @Override
  public ExtensionBrakeStates getExtensionBrakeState() {
    return ExtensionBrakeStates.UNKNOWN;
  }
}
