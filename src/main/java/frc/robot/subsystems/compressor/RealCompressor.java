/// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.compressor;

import edu.wpi.first.wpilibj.Compressor;

/** Add your docs here. */
public class RealCompressor extends CompressorBase {
  private Compressor m_compressor;

  public RealCompressor() {
    m_compressor = new Compressor();
  }

  public void start() {
    m_compressor.start();
  }
}
