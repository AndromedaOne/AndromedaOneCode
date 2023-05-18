// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmRotate;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MockSamArmRotate implements SamArmRotateBase {
  @Override
  public void rotate(double speed) {
  }

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public void engageArmBrake() {

  }

  @Override
  public void disengageArmBrake() {

  }

  public ArmAngleBrakeState getState() {
    return ArmAngleBrakeState.ENGAGEARMBRAKE;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
  }

  @Override
  public void stop() {
  }
}
