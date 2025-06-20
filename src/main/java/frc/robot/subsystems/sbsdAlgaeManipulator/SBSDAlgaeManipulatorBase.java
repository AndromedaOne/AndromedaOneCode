// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdAlgaeManipulator;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface SBSDAlgaeManipulatorBase extends SubsystemInterface {

  Object m_deployAlgaeManipulator = null;

  public abstract void runWheelsToIntake();

  public abstract void runWheelsToEject();

  public abstract void stopAlgaeManipulatorIntakeWheels();

  public abstract void moveUsingSmartDashboard(double speed);

  public abstract boolean isAlgaeManipulatorOnTarget();

  public abstract void reloadConfig();

  public abstract void resetAlgaeManipulatorAngle();

  public abstract void setAlgaeManipulatorAngleSetpoint(double angle);

  public abstract void initializeSpeed();

  public abstract void rotateForInitialize(double speed);

  public abstract void setInitialized();

  public abstract boolean getAlgaeManipMaxAngleLimitSwitchState();
}
