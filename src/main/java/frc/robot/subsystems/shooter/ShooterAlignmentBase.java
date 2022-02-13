// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShooterAlignmentBase extends SubsystemBase {
  /** Creates a new ShooterAlignmentBase. */
  public ShooterAlignmentBase() {
  }

  public abstract void rotateShooter(double speed);

  public abstract boolean atTopLimitSwitch();

  public abstract boolean atBottomLimitSwitch();

  public abstract double getAngle();

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
