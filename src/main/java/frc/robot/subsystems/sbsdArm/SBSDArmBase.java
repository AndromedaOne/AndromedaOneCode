// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdArm;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface SBSDArmBase extends SubsystemInterface {

  public abstract void setPosition(double angle);

  public void stop();

  public abstract double getAngleDeg();

  public abstract double getAngleRad();

  public abstract void setCoastMode();

  public abstract void setBrakeMode();

  public abstract void rotate(double speed);

  public abstract void setGoalDeg(double goal);

  public abstract void reloadConfig();

  public abstract void calculateSpeed();

}
