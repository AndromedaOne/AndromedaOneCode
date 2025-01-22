// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armTestBed;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockArmTestBed implements ArmTestBedBase {

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
  public double getAngularVelDeg() {
    return 0.0;
  }

  @Override
  public void setCoastMode() {
  }

  @Override
  public void setBrakeMode() {
  }

  @Override
  public void rotate(double speed) {
  }

  @Override
  public Command sysIdQuasistatic(Direction direction) {
    return null;
  }

  @Override
  public Command sysIdDynamic(Direction direction) {
    return null;
  }

  @Override
  public void setGoal(double goal) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'setGoal'");
  }

  @Override
  public void calculateAndSetVoltageForGoal() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'calculateVoltageForGoal'");
  }

  @Override
  public double getAngleRad() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAngleRad'");
  }

  @Override
  public double getAngularVelRad() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAngularVelRad'");
  }
}
