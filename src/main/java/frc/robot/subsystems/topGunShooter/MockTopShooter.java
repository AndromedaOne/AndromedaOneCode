// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunShooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public class MockTopShooter implements ShooterWheelBase {

  @Override
  public void setShooterWheelPower(double power) {

  }

  @Override
  public double getShooterWheelPower() {

    return 0;
  }

  @Override
  public double getShooterWheelRpm() {

    return 0;
  }

  @Override
  public String getShooterName() {
    return "mockTopShooter";
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(Command command) {
  }
}
