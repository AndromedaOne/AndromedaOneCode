// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MockSamArmExtRet implements SamArmExtRetBase {
  /** Creates a new MockSamArmExtension. */
  public MockSamArmExtRet() {
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

  @Override
  public SubsystemBase getSubsystemBase() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
    throw new UnsupportedOperationException("Unimplemented method 'setDefaultCommand'");
  }
}
