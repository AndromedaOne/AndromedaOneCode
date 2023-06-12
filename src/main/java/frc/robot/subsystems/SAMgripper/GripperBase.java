// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.SAMgripper;

import frc.robot.subsystems.SubsystemInterface;

/** Add your docs here. */
public interface GripperBase extends SubsystemInterface {

  public void initialize();

  public void openGripper();

  public void closeGripper();

  public GripperState getState();

}
