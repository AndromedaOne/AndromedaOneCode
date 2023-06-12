// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotCannon;

import frc.robot.subsystems.SubsystemInterface;

public interface CannonBase extends SubsystemInterface {
  public abstract void pressurize();

  public abstract boolean shoot();

  public abstract void reset();

  public abstract boolean isPressurized();

  public abstract boolean isCannonElevationInRange();
}
