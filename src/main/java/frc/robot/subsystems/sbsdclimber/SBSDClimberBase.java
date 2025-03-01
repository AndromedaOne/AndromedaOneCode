// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdclimber;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface SBSDClimberBase extends SubsystemInterface {
  public abstract void climb();

  public abstract void reverseClimb();

  public abstract void stop();

  public abstract void setServoInitialPosition();

}
