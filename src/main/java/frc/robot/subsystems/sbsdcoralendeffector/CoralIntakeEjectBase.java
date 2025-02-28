// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import frc.robot.oi.DriveController;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface CoralIntakeEjectBase extends SubsystemInterface {

  public abstract void runWheels(double speed);

  public abstract void runWheelsIntake();

  public abstract void runWheelsEject();

  public abstract void setEjectState();

  public abstract boolean getCoralDetected();

  public abstract boolean hasScored();

  public void stop();

  public abstract boolean intakeDetector();

  public abstract boolean ejectDetector();

  public abstract void setCoastMode();

  public abstract void setBrakeMode();

  public abstract void exitL4ScoringPosition();

  public abstract void setDriveController(DriveController driveController);

  public abstract void scoreL4();

  public abstract void setSubsystemController(SubsystemController subsystemController);
}
