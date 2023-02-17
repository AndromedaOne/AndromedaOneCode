/// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.compressor;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.Config4905;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class RealCompressor extends CompressorBase {
  private Compressor m_compressor;

  public RealCompressor() {
    Config compressorConfig = Config4905.getConfig4905().getCompressorConfig();
    if (compressorConfig.hasPath("ModuleType")) {
      Trace.getInstance().logInfo("Line 21 in RealCompressor ran");
      String moduleString = compressorConfig.getString("ModuleType");
      Trace.getInstance().logInfo(moduleString);
      if (moduleString.equals("REVPH")) {
        // if (compressorConfig.getString("ModuleType") == "REVPH") {
        int portInt = compressorConfig.getInt("port");
        m_compressor = new Compressor(portInt, PneumaticsModuleType.REVPH);
        System.out.println("compresser config type " + m_compressor.getConfigType().toString());
        // }
      }
    } else {
      m_compressor = new Compressor(PneumaticsModuleType.CTREPCM);
    }
  }

  public void start() {
    m_compressor.enableDigital();
  }
}
