// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.wpilib.command2.Command;
import org.wpilib.command2.SubsystemBase;

/** Add your docs here. */
public interface SubsystemInterface {
  public SubsystemBase getSubsystemBase();

  public void setDefaultCommand(Command command);
}
