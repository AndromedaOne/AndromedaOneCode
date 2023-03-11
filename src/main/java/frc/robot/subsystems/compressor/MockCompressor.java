// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.compressor;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

/** Add your docs here. */
public class MockCompressor extends CompressorBase {
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
}
