// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.SAMgripper;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;

/** Add your docs here. */
public class RealGripper extends GripperBase {
  private Config m_config;
  // private DoubleSolenoid4905 m_gripperSolenoid;
  private DoubleSolenoid4905 m_solenoid7_0;

  private static final int OPENGRIPPER = 0;
  private static final int CLOSEGRIPPER = 1;

  public RealGripper() {
    m_config = Config4905.getConfig4905().getGripperConfig();
    m_solenoid7_0 = new DoubleSolenoid4905(m_config, "solenoid7_0");
  }

  @Override
  public void initialize() {

  }

  @Override
  public void openGripper() {
    // retracts piston
    m_solenoid7_0.retractPiston();
  }

  @Override
  public void closeGripper() {
    // extends piston
    m_solenoid7_0.extendPiston();
  }

  public int getState() {
    if (m_solenoid7_0.isSolenoidOpen()) {
      return OPENGRIPPER;
    } else {
      return CLOSEGRIPPER;
    }
  }

}
