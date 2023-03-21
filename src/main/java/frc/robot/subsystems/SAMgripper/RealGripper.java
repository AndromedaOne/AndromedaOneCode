// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.SAMgripper;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.subsystems.compressor.CompressorBase;

/** Add your docs here. */
public class RealGripper extends GripperBase {
  private Config m_config;
  // private DoubleSolenoid4905 m_gripperSolenoid;
  private DoubleSolenoid4905 m_solenoid0_1;
  private GripperState m_gripperState = GripperState.CLOSEGRIPPER;

  public RealGripper(CompressorBase compressorBase) {
    requireNonNullParam(compressorBase, "compressorBase", "RealGripper Constructor");
    m_config = Config4905.getConfig4905().getGripperConfig();
    m_solenoid0_1 = new DoubleSolenoid4905(compressorBase, m_config, "solenoid0_1");
  }

  @Override
  public void initialize() {
    closeGripper();
    m_gripperState = GripperState.CLOSEGRIPPER;

  }

  @Override
  public void openGripper() {
    // retracts piston
    m_solenoid0_1.extendPiston();
    System.out.println("extend piston");
    m_gripperState = GripperState.OPENGRIPPER;
  }

  @Override
  public void closeGripper() {
    // extends piston
    m_solenoid0_1.retractPiston();
    System.out.println("retract piston");
    m_gripperState = GripperState.CLOSEGRIPPER;
  }

  public GripperState getState() {
    return m_gripperState;
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("Real Gripper state", getState().name());
  }
}
