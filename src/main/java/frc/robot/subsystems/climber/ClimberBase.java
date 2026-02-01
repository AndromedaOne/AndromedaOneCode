// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.climber;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface ClimberBase extends SubsystemInterface {

  public void rotateRotator(double speed);

  public double getRotatorAngle();

  public void moveExtender(double speed);

  public void setBrakeMode();

  public void setCoastMode();

}