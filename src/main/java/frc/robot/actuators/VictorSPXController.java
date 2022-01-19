// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.typesafe.config.Config;

/** Add your docs here. */
public class VictorSPXController {
  private WPI_VictorSPX m_victorSPX;
  private boolean m_hasEncoder = false;

  public VictorSPXController(Config subsystemConfig, String configString) {
    m_victorSPX = new WPI_VictorSPX(subsystemConfig.getInt("ports." + configString));
    configure(subsystemConfig, configString);
    m_hasEncoder = subsystemConfig.getBoolean(configString + ".hasEncoder");
    System.out.println("Creating new VictorSPX from port: " + configString);
  }

  private void configure(Config subsystemConfig, String configString) {
    m_victorSPX.configFactoryDefault();
    if (subsystemConfig.getBoolean(configString + ".brakeMode")) {
      m_victorSPX.setNeutralMode(NeutralMode.Brake);
    } else {
      m_victorSPX.setNeutralMode(NeutralMode.Coast);
    }
    m_victorSPX.setInverted(subsystemConfig.getBoolean(configString + ".inverted"));
  }

  public boolean hasEncoder() {
    return m_hasEncoder;
  }

  public void set(double speed) {
    m_victorSPX.set(speed);
  }
}
