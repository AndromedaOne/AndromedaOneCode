// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armhopperintake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.MockSubsystem;

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
  public void rotateArm(double speed) {
  }

  @Override
  public void moveHopper(double speed) {
  }

  @Override
  public double getArmAngle() {
    return 0;
  }

  @Override
  public double getHopperAngle() {
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

}
