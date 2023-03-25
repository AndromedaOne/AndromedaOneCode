// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalOutput;

/** Add your docs here. */
public class DigitalOutput4905 {
  private DigitalOutput m_digitalOutput;

  public DigitalOutput4905(Config subsystemConfig, String confString) {
    m_digitalOutput = new DigitalOutput(subsystemConfig.getInt(confString + "port"));
  }

  public void setValue(boolean value) {
    m_digitalOutput.set(value);
  }

  public boolean getValue() {
    return m_digitalOutput.get();
  }
}
