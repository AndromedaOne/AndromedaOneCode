// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface ShooterBase extends SubsystemInterface {
  final static String s_shooterString = "shooter/";

  /**
   * In RPM
   */
  public double getShooterVelocity();

  /**
   * Run this in the command
   */
  public void runShooter(double power);

  public void stop();

  public void setBrakeMode();

  public void setSetpointStatus(boolean isAtSetpoint);

  public boolean isAtRPMSetpoint();

  public static String getSmartDashboardShooterString() {
    return s_shooterString;
  }

  public void setCoastMode();

}