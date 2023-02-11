// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.SAMgripper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public abstract class GripperBase extends SubsystemBase {
  public GripperBase() {

  }

  public abstract void initialize();

  public abstract void openGripper();

  public abstract void closeGripper();

  public abstract int getState();

  @Override
  public void periodic() {
// this method will be called once per scheduler run
  }

}
