package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class DoubleSolenoid4905 extends DoubleSolenoid {
  public DoubleSolenoid4905(Config subsystemConfig, String configString) {
    super(subsystemConfig.getInt("ports." + configString + ".forwardChannel"),
        subsystemConfig.getInt("ports." + configString + ".reverseChannel"));
  }

  public void extendPiston() {
    set(Value.kForward);
  }

  public void retractPiston() {
    set(Value.kReverse);
  }

}