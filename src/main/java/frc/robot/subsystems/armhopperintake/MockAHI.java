// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armhopperintake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;
import frc.robot.subsystems.armhopperintake.RealAHI.State;

/** Add your docs here. */
public class MockAHI implements AHIBase {

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void rotateArm(double speed, boolean override) {
  }

  @Override
  public double getArmAngle() {
    return 0;
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
  public void rotateArmPID() {
  }

  @Override
  public void setArmSetpoint(double setpoint) {
  }

  @Override
  public void setState(State state) {
  }

  @Override
  public State getState() {
    return State.RETRACTED;
  }

  @Override
  public boolean atSetpoint() {
    return true;
  }

  @Override
  public void setRetracting() {
  }

  @Override
  public void setExtending() {
  }

  @Override
  public boolean isRetracting() {
    return false;
  }

  @Override
  public boolean isExtending() {
    return false;
  }

  @Override
  public void setArmPID(double kP, double kI, double kD) {
  }

}
