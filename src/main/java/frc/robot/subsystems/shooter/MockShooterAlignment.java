// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

public class MockShooterAlignment extends ShooterAlignmentBase {
  /** Creates a new MockShooterAlignment. */
  public MockShooterAlignment() {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
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

}
