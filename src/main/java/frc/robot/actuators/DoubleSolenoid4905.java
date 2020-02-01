package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DoubleSolenoid4905 {
  private DoubleSolenoid m_doubleSolenoid;

  public DoubleSolenoid4905(Config subsystemConfig, String configString) {
    m_doubleSolenoid = new DoubleSolenoid(subsystemConfig.getInt("ports." + configString + ".forwardChannel"),
        subsystemConfig.getInt("ports." + configString + ".reverseChannel"));
  }

  public void extendPiston() {
    m_doubleSolenoid.set(DoubleSolenoid.Value.kForward);
  }

  public void retractPiston() {
    m_doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

}