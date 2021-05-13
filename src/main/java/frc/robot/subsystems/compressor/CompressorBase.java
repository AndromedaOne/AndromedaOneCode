// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.compressor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class CompressorBase extends SubsystemBase {
  /** Creates a new CompressorBase. */
  public CompressorBase() {
  }

  public abstract void start();

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
