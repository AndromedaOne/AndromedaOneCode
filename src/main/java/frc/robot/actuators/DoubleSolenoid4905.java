package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DoubleSolenoid4905 {
  private DoubleSolenoid m_doubleSolenoid;
  // We are assuming the solenoid starts closed
  private boolean m_isSolenoidOpen = false;

  public DoubleSolenoid4905(Config subsystemConfig, String configString) {
    m_doubleSolenoid = new DoubleSolenoid(subsystemConfig.getInt("ports." + configString + ".forwardChannel"),
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

  public boolean isSolenoidOpen() {
    return m_isSolenoidOpen;
  }
}