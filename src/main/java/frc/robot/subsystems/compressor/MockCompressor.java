// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.compressor;

import org.wpilib.hardware.pneumatic.PneumaticsModuleType;
import org.wpilib.command2.Command;
import org.wpilib.command2.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

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
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }
}
