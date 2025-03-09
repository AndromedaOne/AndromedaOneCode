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
  public void moveUsingSmartDashboard(double speed) {
  }

  @Override
  public boolean isAlgaeManipulatorOnTarget() {
    return true;
  }

  @Override
  public void reloadConfig() {
  }

  @Override
  public void resetAlgaeManipulatorAngle() {
  }

  @Override
  public void setAlgaeManipulatorAngleSetpoint(double angle) {
  }

  @Override
  public void initializeSpeed() {
  }

  @Override
  public void rotateForInitialize(double speed) {
  }

  @Override
  public void setInitialized() {
  }

  @Override
  public boolean getAlgaeManipMaxAngleLimitSwitchState() {
    return false;
  }
}
