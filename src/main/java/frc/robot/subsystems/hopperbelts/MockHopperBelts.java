// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.hopperbelts;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockHopperBelts implements HopperBeltsBase {
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  public void setDefaultCommand(Command command) {

  }

  @Override
  public void stop() {
  }

  @Override
  public void intake() {
  }

  @Override
  public void eject() {
  }

  @Override
  public void ejectThroughIntake() {
  }

  @Override
  public void setBrakeMode() {
  }

  @Override
  public void setCoastMode() {
  }
}
