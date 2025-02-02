// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.powerDistribution4905;

import edu.wpi.first.wpilibj.PowerDistribution;

/** Add your docs here. */
public class PowerDistributionHub extends PowerDistributionBase {

  private PowerDistribution m_powerDistributionHub;

  public PowerDistributionHub(int canID) {
    m_powerDistributionHub = new PowerDistribution(canID, PowerDistribution.ModuleType.kRev);
  }

  @Override
  public double getVoltage() {
    return m_powerDistributionHub.getVoltage();
  }
}
