// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdAlgaeManipulator;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface SBSDAlgaeManipulatorBase extends SubsystemInterface {

  public abstract void runWheelsToIntake();

  public abstract void runWheelsToEject();

  public abstract void stopAlgaeManipulatorIntakeWheels();

  public abstract void moveAlgaeManipulatorUsingPID();

  public abstract void moveUsingSmartDashboard(double speed);

  public abstract void setDeploySetpoint();

  public abstract void setRetractSetpoint();

  public abstract boolean isAlgaeRotateOnTarget();
}
