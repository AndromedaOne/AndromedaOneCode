// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armhopperintake;

import frc.robot.subsystems.SubsystemInterface;
import frc.robot.subsystems.armhopperintake.RealAHI.State;

/** Add your docs here. */
public interface AHIBase extends SubsystemInterface {

  public void rotateArm(double speed);

  public void moveHopper(double speed);

  public double getArmAngle();

  public double getHopperAngle();

  public void stop();

  public void rotateArmPID();

  public void setArmSetpoint(double setpoint);

  public void moveHopperPID();

  public void setHopperSetpoint(double setpoint);

  public void setState(State state);

  public State getState();

  public void setBrakeMode();

  public void setCoastMode();

}
