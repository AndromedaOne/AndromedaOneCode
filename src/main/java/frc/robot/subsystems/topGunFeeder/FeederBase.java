// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunFeeder;

import frc.robot.subsystems.SubsystemInterface;

public interface FeederBase extends SubsystemInterface {
  public abstract void runFeeder(double speed);

  public abstract void stopFeeder();

  public abstract void runFeederInReverse(double speed);
}
