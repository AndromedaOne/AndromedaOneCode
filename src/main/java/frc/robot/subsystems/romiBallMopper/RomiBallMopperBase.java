// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.romiBallMopper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class RomiBallMopperBase extends SubsystemBase {
  /** Creates a new RomiBallMopperBase. */
  static private boolean s_reset = true;

  public RomiBallMopperBase() {
  }

  public abstract void mop();

  public abstract void reset();

  public boolean getResetState() {
    return s_reset;
  }

  public void setResetState(boolean state) {
    s_reset = state;
  }

  public abstract boolean doesRomiBallMopperExist();

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
