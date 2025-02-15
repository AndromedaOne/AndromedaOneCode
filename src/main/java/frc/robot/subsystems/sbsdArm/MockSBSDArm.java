// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdArm;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.sbsdArmCommands.ArmSetpoints;
import frc.robot.subsystems.MockSubsystem;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

/** Add your docs here. */
public class MockSBSDArm implements SBSDArmBase {
  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void setPosition(double angle) {
  }

  @Override
  public void stop() {
  }

  @Override
  public double getAngleDeg() {
    return 0;
  }

  @Override
  public void setCoastMode() {
  }

  @Override
  public void setBrakeMode() {
  }

  @Override
  public void setEndEffector(CoralEndEffectorRotateBase endEffector) {
  }

  @Override
  public boolean atSetPoint() {
    return false;
  }

  @Override
  public boolean limitSwitchActive() {
    return false;
  }

  @Override
  public void rotate(double speed) {
  }

  @Override
  public void setGoalDeg(double goal) {
  }

  @Override
  public void setGoalDeg(ArmSetpoints setpoint) {
  }

  @Override
  public double getAngleRad() {
    return 0;
  }

  @Override
  public void reloadConfig() {
  }

  @Override
  public void calculateSpeed() {
  }

  @Override
  public void runAlgaeRemovalWheels() {
  }
}

