// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.powerDistribution4905;

import edu.wpi.first.wpilibj.PowerDistribution;

/** Add your docs here. */

public class PowerDistributionPanel extends PowerDistributionBase {

  private PowerDistribution m_powerDistributionPanel;

  public PowerDistributionPanel() {
    m_powerDistributionPanel = new PowerDistribution();
  }

  @Override
  public double getVoltage() {
    return m_powerDistributionPanel.getVoltage();
  }
}
