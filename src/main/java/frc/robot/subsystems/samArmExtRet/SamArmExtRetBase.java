// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class SamArmExtRetBase extends SubsystemBase {
  /** Creates a new SamArmExtensionBase. */
  public SamArmExtRetBase() {
  }

  // Arms extending is positive, arms retracting is negative.
  public abstract void extendRetract(double speed);

  public abstract double getPosition();

  public abstract void setZeroOffset();
}
