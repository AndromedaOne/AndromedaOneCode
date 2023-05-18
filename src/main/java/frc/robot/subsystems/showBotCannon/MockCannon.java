// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotCannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public class MockCannon implements CannonBase {

  @Override
  public void pressurize() {

  }

  @Override
  public void shoot() {

  }

  @Override
  public boolean isPressurized() {

    return false;
  }

  @Override
  public void reset() {

  }

  @Override
  public boolean isCannonElevationInRange() {
    return false;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
  }
}
