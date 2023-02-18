package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class DoubleSolenoid4905 {
  private DoubleSolenoid m_doubleSolenoid;
  // We are assuming the solenoid starts closed
  private boolean m_isSolenoidOpen = false;

  public DoubleSolenoid4905(Config subsystemConfig, String configString) {
    if (subsystemConfig.hasPath("ModuleType")) {
      String moduleString = subsystemConfig.getString("ModuleType");
      if (moduleString.equals("REVPH")) {
        // if (subsystemConfig.getString("ModuleType") == "REVPH") {
        m_doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH,
            subsystemConfig.getInt("ports." + configString + ".forwardChannel"),
            subsystemConfig.getInt("ports." + configString + ".reverseChannel"));
      }
      // }
    } else {
      m_doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM,
          subsystemConfig.getInt("ports." + configString + ".forwardChannel"),
          subsystemConfig.getInt("ports." + configString + ".reverseChannel"));
    }
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