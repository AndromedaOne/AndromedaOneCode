// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.feeder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class FeederBase extends SubsystemBase {
  /** Creates a new FeederBase. */
  public FeederBase() {
  }

  public abstract void runFeeder(double speed);

  public abstract void stopFeeder();

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
