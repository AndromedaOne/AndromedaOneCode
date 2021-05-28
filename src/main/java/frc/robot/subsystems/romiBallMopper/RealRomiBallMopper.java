// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.romiBallMopper;

import frc.robot.Config4905;
import frc.robot.actuators.ServoMotor;

/** Add your docs here. */
public class RealRomiBallMopper extends RomiBallMopperBase {
  private ServoMotor m_motor;

  public RealRomiBallMopper() {
    m_motor = new ServoMotor(Config4905.getConfig4905().getRomiBallMopperConfig().getInt("port"));
  }

  @Override
  public void mop() {
    m_motor.runBackward();
  }

  @Override
  public void reset() {
    m_motor.runForward();
  }

}