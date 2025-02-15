// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdclimber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockSBSDClimber implements SBSDClimberBase {

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void climb() {
  }

  @Override
  public void reverseClimb() {
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

}
