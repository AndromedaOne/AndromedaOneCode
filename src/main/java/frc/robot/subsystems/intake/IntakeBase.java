// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class intake extends SubsystemBase {
  /** Creates a new intake. */
  // public intake() {
  // }

  public abstract void runIntake(double speed);

  public abstract void stopIntake();

  public abstract void deployIntake();

  public abstract void retractIntake();

  // @Override
  // public void periodic() {
  // This method will be called once per scheduler run
  // }
}
