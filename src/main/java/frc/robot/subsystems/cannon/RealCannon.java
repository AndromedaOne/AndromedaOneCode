// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.cannon;

import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;

/** Add your docs here. */
public class RealCannon extends CannonBase {
  private DoubleSolenoid4905 m_solenoid0_7;
  private DoubleSolenoid4905 m_solenoid1_6;
  private DoubleSolenoid4905 m_solenoid2_5;
  private DoubleSolenoid4905 m_solenoid3_4;

  public RealCannon() {
    m_solenoid0_7 = new DoubleSolenoid4905(Config4905.getConfig4905().getCannonConfig(), "solenoid0_7");
    m_solenoid1_6 = new DoubleSolenoid4905(Config4905.getConfig4905().getCannonConfig(), "solenoid1_6");
    m_solenoid2_5 = new DoubleSolenoid4905(Config4905.getConfig4905().getCannonConfig(), "solenoid2_5");
    m_solenoid3_4 = new DoubleSolenoid4905(Config4905.getConfig4905().getCannonConfig(), "solenoid3_4");
  }

  @Override
  public void pressurize() {
    m_solenoid0_7.extendPiston();
    m_solenoid1_6.extendPiston();
    m_solenoid2_5.extendPiston();
    m_solenoid3_4.extendPiston();
  }

  @Override
  public void shoot() {
    m_solenoid0_7.retractPiston();
    m_solenoid1_6.retractPiston();
    m_solenoid2_5.retractPiston();
    m_solenoid3_4.retractPiston();
  }

  @Override
  public boolean isPressurized() {

    return false;
  }
}