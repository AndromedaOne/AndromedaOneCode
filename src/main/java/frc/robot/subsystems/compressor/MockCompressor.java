// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.compressor;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public class MockCompressor implements CompressorBase {
  public void start() {

  }

  @Override
  public int getPortNumber() {
    return 0;
  }

  @Override
  public PneumaticsModuleType getCompressorModuleType() {
    return null;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(Command command) {
    throw new UnsupportedOperationException("Unimplemented method 'setDefaultCommand'");
  }
}
