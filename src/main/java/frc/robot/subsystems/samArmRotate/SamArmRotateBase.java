// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmRotate;

import frc.robot.subsystems.SubsystemInterface;

public interface SamArmRotateBase extends SubsystemInterface {

  public abstract void rotate(double speed);

  public void stop();

  public abstract double getAngle();

  public abstract void engageArmBrake();

  public abstract void disengageArmBrake();

  public abstract ArmAngleBrakeState getState();

}
