// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunShooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class MockShooterAlignment implements ShooterAlignmentBase {
  /** Creates a new MockShooterAlignment. */
  public MockShooterAlignment() {
  }

  @Override
  public void rotateShooter(double speed) {

  }

  @Override
  public boolean atTopLimitSwitch() {
    return false;
  }

  @Override
  public boolean atBottomLimitSwitch() {
    return false;
  }

  @Override
  public double getAngle() {
    return 0;
  }

  @Override
  public String getShooterName() {
    return "MockShooterAlignment";
  }

  @Override
  public void stopShooterAlignment() {
  }

  @Override
  public void setCoastMode() {

  }

  @Override
  public void setBrakeMode() {

  }

  @Override
  public void extendShooterArms() {

  }

  @Override
  public void stowShooterArms() {

  }

  @Override
  public SubsystemBase getSubsystemBase() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
  }

  @Override
  public boolean getInitialized() {
    return false;
  }

  @Override
  public void setInitialized() {
  }

  @Override
  public void setOffset(double offset) {
  }

  @Override
  public double getOffset() {
    return 0;
  }
}
