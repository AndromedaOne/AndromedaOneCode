/// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.compressor;

import com.typesafe.config.Config;

import org.wpilib.hardware.pneumatic.Compressor;
import org.wpilib.hardware.pneumatic.PneumaticsModuleType;
import org.wpilib.system.SensorUtil;
import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.command2.Command;
import org.wpilib.command2.SubsystemBase;
import frc.robot.Config4905;

/** Add your docs here. */
public class RealCompressor extends SubsystemBase implements CompressorBase {
  private Compressor m_compressor;
  private int m_portInt = 0;
  private PneumaticsModuleType m_compressorModuleType;

  public RealCompressor() {
    Config compressorConfig = Config4905.getConfig4905().getCompressorConfig();
    if (compressorConfig.hasPath("ModuleType")) {
      String moduleString = compressorConfig.getString("ModuleType");
      if (moduleString.equals("REV_PH")) {
        m_portInt = compressorConfig.getInt("port");
        m_compressor = new Compressor(m_portInt, PneumaticsModuleType.REV_PH);
        m_compressorModuleType = PneumaticsModuleType.REV_PH;
      } else if (moduleString.equals("CTRE_PCM")) {
        m_compressor = new Compressor(PneumaticsModuleType.CTRE_PCM);
        m_compressorModuleType = PneumaticsModuleType.CTRE_PCM;
        m_portInt = SensorUtil.getDefaultCTRE_PCMModule();
      } else {
        throw (new RuntimeException("Unknown PCM Module Type"));
      }
    } else {
      m_compressor = new Compressor(PneumaticsModuleType.CTRE_PCM);
      m_compressorModuleType = PneumaticsModuleType.CTRE_PCM;
      m_portInt = SensorUtil.getDefaultCTRE_PCMModule();
    }
  }

  public void start() {
    m_compressor.enableDigital();
  }

  @Override
  public int getPortNumber() {
    return m_portInt;
  }

  @Override
  public PneumaticsModuleType getCompressorModuleType() {
    return m_compressorModuleType;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Pneumatics Pressure", m_compressor.getPressure());
  }
}
