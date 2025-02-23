// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockCoralEndEffectorRotate implements CoralEndEffectorRotateBase {

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void setAngleDeg(double angle) {
  }

  @Override
  public void stop() {
  }

  @Override
  public double getAngleDeg() {
    return 0;
  }

  @Override
  public double getAngleRad() {
    return 0;
  }

  @Override
  public void setCoastMode() {
  }

  @Override
  public void setBrakeMode() {
  }

  @Override
  public boolean atSetPoint() {
    return true;
  }

  @Override
  public boolean isEndEffectorSafe() {
    return false;
  }

  @Override
  public void rotate(double speed) {
  }

  @Override
  public void reloadConfig() {
  }

  @Override
  public void calculateSpeed() {
  }

  @Override
  public double getSafeAngleToScoreL4() {
    return 0;
  }
}
