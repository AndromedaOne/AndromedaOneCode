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
  private DoubleSolenoid4905 m_solenoid7_8;
  private GripperState m_gripperState = GripperState.CLOSEGRIPPER;

  public RealGripper() {
    m_config = Config4905.getConfig4905().getGripperConfig();
    m_solenoid7_8 = new DoubleSolenoid4905(m_config, "solenoid7_8");
  }

  @Override
  public void initialize() {
    closeGripper();
    m_gripperState = GripperState.CLOSEGRIPPER;

  }

  @Override
  public void openGripper() {
    // retracts piston
    m_solenoid7_8.retractPiston();
    System.out.println("retract piston");
    m_gripperState = GripperState.OPENGRIPPER;
  }

  @Override
  public void closeGripper() {
    // extends piston
    m_solenoid7_8.extendPiston();
    System.out.println("extend piston");
    m_gripperState = GripperState.CLOSEGRIPPER;
  }

  public GripperState getState() {
    return m_gripperState;
  }

}
