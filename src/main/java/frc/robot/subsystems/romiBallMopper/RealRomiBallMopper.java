// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.romiBallMopper;

import frc.robot.Config4905;
import frc.robot.actuators.HitecHS322HDpositionalServoMotor;
import frc.robot.actuators.ServoMotorPositional;

/** Add your docs here. */
public class RealRomiBallMopper extends RomiBallMopperBase {
  private ServoMotorPositional m_motor;

  public RealRomiBallMopper() {
    m_motor = new HitecHS322HDpositionalServoMotor(
        Config4905.getConfig4905().getRomiBallMopperConfig(), "MopperServoMotor");
  }

  @Override
  public void mop() {
    m_motor.set(0.5);
  }

  @Override
  public void reset() {
    m_motor.set(-0.5);
  }

  @Override
  public boolean doesRomiBallMopperExist() {
    return true;
  }
}