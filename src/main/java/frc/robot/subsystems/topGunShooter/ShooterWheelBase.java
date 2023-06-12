// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunShooter;

import frc.robot.subsystems.SubsystemInterface;

public interface ShooterWheelBase extends SubsystemInterface {
  public abstract void setShooterWheelPower(double power);

  public abstract double getShooterWheelPower();

  public abstract double getShooterWheelRpm();

  public abstract String getShooterName();
}
