// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmRotate;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class SamArmRotateBase extends SubsystemBase {
  /** Creates a new SamArmRotateBase. */
  public SamArmRotateBase() {
  }

  public abstract void rotate(double speed);

  public void stop() {
    rotate(0);
  }

  public abstract double getAngle();

  public abstract boolean getInitialized();

  public abstract void setInitialized();

  public abstract void engageArmBrake();

  public abstract void disengageArmBrake();

  public abstract ArmAngleBrakeState getState();

}
