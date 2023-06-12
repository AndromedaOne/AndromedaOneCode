// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import frc.robot.subsystems.SubsystemInterface;

public interface SamArmExtRetBase extends SubsystemInterface {

  public enum RetractLimitSwitchState {
    CLOSED, OPEN;
  }

  // Arms extending is positive, arms retracting is negative.
  public void extendRetract(double speed);

  public double getPosition();

  public void setZeroOffset();

  public RetractLimitSwitchState getRetractLimitSwitchState();

  public void retractArmInitialize();

  public ExtensionBrakeStates getExtensionBrakeStates();

  public boolean isInitialized();

  public void setInitialized();

  public void engageArmBrake();

  public void disengageArmBrake();

  public ExtensionBrakeStates getExtensionBrakeState();
}
