package frc.robot.actuators;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.subsystems.compressor.CompressorBase;

public class DoubleSolenoid4905 {
  private DoubleSolenoid m_doubleSolenoid;
  // We are assuming the solenoid starts closed
  private boolean m_isSolenoidOpen = false;

  public DoubleSolenoid4905(CompressorBase compressorBase, Config subsystemConfig,
      String configString) {
    requireNonNullParam(compressorBase, "compressorBase", "DoubleSolenoid4905 Constructor");
    m_doubleSolenoid = new DoubleSolenoid(compressorBase.getPortNumber(),
        compressorBase.getCompressorModuleType(),
        subsystemConfig.getInt("ports." + configString + ".forwardChannel"),
        subsystemConfig.getInt("ports." + configString + ".reverseChannel"));
  }

  public void extendPiston() {
    m_doubleSolenoid.set(DoubleSolenoid.Value.kForward);
    m_isSolenoidOpen = true;
  }

  public void retractPiston() {
    m_doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
    m_isSolenoidOpen = false;
  }

  public void stopPiston() {
    m_doubleSolenoid.set(DoubleSolenoid.Value.kOff);
  }

  public boolean isSolenoidOpen() {
    return m_isSolenoidOpen;
  }
}