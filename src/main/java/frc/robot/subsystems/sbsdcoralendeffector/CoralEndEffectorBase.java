// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface CoralEndEffectorBase extends SubsystemInterface {

  public abstract void eject();

  public abstract void intake();

  public abstract void stop();

  public abstract boolean hasCoral();

  public abstract void setAngle(double angle);

}
