// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

/** Add your docs here. */
public class MockClimber implements ClimberBase {

  @Override
  public SubsystemBase getSubsystemBase() {
    return new MockSubsystem();
  }

  @Override
  public void setDefaultCommand(Command command) {
  }

  @Override
  public void rotateRotator(double speed) {
  }

  @Override
  public double getRotatorAngle() {
    return 0;
  }

  @Override
  public void moveExtender(double speed) {
  }

  @Override
  public void setBrakeMode() {
  }

  @Override
  public void setCoastMode() {
  }

}
