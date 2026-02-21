// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armhopperintake;

import frc.robot.subsystems.SubsystemInterface;
import frc.robot.subsystems.armhopperintake.RealAHI.State;

/** Add your docs here. */
public interface AHIBase extends SubsystemInterface {

  public void rotateArm(double speed, boolean override);

  public double getArmAngle();

  public void stop();

  public void rotateArmPID();

  public void setArmSetpoint(double setpoint);

  public void setState(State state);

  public State getState();

  public boolean atSetpoint();

  public void setRetracting();

  public void setExtending();

  public boolean isRetracting();

  public boolean isExtending();

  public void setArmPID(double kP, double kI, double kD);

  public void setBrakeMode();

  public void setCoastMode();

}
