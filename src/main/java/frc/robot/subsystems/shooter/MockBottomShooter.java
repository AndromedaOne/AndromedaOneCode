// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import org.wpilib.command2.Command;
import org.wpilib.command2.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockBottomShooter implements ShooterBase {

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public double getShooterVelocity() {
    return 0;
  }

  @Override
  public void runShooter(double power) {
  }

  @Override
  public void stop() {
  }

  @Override
  public void setBrakeMode() {
  }

  @Override
  public void setCoastMode() {
  }

  @Override
  public void setSetpointStatus(boolean isAtSetpoint) {
  }

  @Override
  public boolean isAtRPMSetpoint() {
    return true;
  }

}
