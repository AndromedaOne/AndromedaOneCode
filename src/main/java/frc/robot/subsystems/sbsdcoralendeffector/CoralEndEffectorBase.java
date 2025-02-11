// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface CoralEndEffectorBase extends SubsystemInterface {

  public abstract void runWheels(double speed);

  public abstract void runWheelsIntake(double speed);

  public void stop();

  public abstract double getAngleDeg();

  public abstract double getAngleRad();

  public abstract void setAngleDeg(double angle);

  public abstract boolean intakeDetector();

  public abstract boolean ejectDetector();

  public abstract void setCoastMode();

  public abstract void setBrakeMode();

  public abstract void rotate(double speed);

  public abstract void reloadConfig();

  public abstract void calculateSpeed();
}
