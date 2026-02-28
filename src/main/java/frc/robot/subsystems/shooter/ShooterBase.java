// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface ShooterBase extends SubsystemInterface {

  public void setVelocitySetpoint(double RPM);

  /**
   * In RPM
   */
  public double getShooterVelocity();

  /**
   * Run this in the command
   */
  public void runShooter();

  public boolean isAtSetpoint();

  public void stop();

  public void setPID(double kp, double ki, double kd);

  public void setBrakeMode();

  public void setCoastMode();

}