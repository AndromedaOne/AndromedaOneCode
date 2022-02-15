// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShooterAlignmentBase extends SubsystemBase {
  private double m_offset = 0;

  private boolean m_initialized = false;

  /** Creates a new ShooterAlignmentBase. */
  public ShooterAlignmentBase() {
  }

  public abstract void rotateShooter(double speed);

  public abstract boolean atTopLimitSwitch();

  public abstract boolean atBottomLimitSwitch();

  public abstract double getAngle();

  public boolean getInitialized() {
    return m_initialized;
  }

  public void setInitialized() {
    m_initialized = true;
  }

  public void setOffset(double offset) {
    m_offset = offset;
  }

  public double getOffset() {
    return m_offset;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
