// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armTestBed;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.subsystems.SubsystemInterface;

public interface ArmTestBedBase extends SubsystemInterface {

  public abstract void setPosition(double angle);

  public void stop();

  public abstract double getAngleDeg();

  public abstract double getAngleRad();

  public abstract double getAngularVelRad();

  public abstract void setCoastMode();

  public abstract void setBrakeMode();

  public abstract void rotate(double speed);

  public abstract Command sysIdQuasistatic(SysIdRoutine.Direction direction);

  public abstract Command sysIdDynamic(SysIdRoutine.Direction direction);

  public abstract void setGoalDeg(double goal);

  public abstract void calculateAndSetVoltageForGoal();
}
