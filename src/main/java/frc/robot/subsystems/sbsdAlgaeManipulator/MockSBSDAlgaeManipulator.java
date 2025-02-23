// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdAlgaeManipulator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockSBSDAlgaeManipulator implements SBSDAlgaeManipulatorBase {

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void runWheelsToIntake() {
  }

  @Override
  public void runWheelsToEject() {
  }

  @Override
  public void stopAlgaeManipulatorIntakeWheels() {
  }

  @Override
  public void deployAlgaeManipulator() {
  }

  @Override
  public void retractAlgaeManipulator() {
  }

  @Override
  public void setDeploySetpoint() {
  }

  @Override
  public void setRetractSetpoint() {
  }

  @Override
  public boolean isAlgaeRotateOnTarget() {
    return true;
  }
}
