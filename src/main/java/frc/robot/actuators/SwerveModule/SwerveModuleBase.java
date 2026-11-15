// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators.SwerveModule;

import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.kinematics.SwerveModulePosition;
import org.wpilib.math.kinematics.SwerveModuleVelocity;

/** Add your docs here. */
public abstract class SwerveModuleBase {

  private int m_moduleNumber = 0;

  public SwerveModuleBase(int moduleNumber) {
    m_moduleNumber = moduleNumber;
  }

  public void setDesiredState(SwerveModuleVelocity desiredState, boolean isOpenLoop,
      boolean overRide) {
    desiredState.optimize(getAngle());
    setAngle(desiredState, overRide);
    setSpeed(desiredState, isOpenLoop);
  }

  public void setDesiredStateNoOptimize(SwerveModuleVelocity desiredState, boolean isOpenLoop,
      boolean overRide) {
    setAngle(desiredState, overRide);
    setSpeed(desiredState, isOpenLoop);
  }

  protected abstract void setSpeed(SwerveModuleVelocity desiredState, boolean isOpenLoop);

  protected abstract void setAngle(SwerveModuleVelocity desiredState, boolean overRide);

  public int getModuleNumber() {
    return m_moduleNumber;
  }

  protected abstract double getAngleMotorRawAngle();

  public Rotation2d getAngle() {
    return Rotation2d.fromDegrees(getAngleMotorRawAngle());
  }

  public abstract double getDriveEncoderPosition();

  public abstract double getDriveEncoderVelocity();

  public SwerveModuleVelocity getState() {
    return new SwerveModuleVelocity(getDriveEncoderPosition(), getAngle());
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(getDriveEncoderPosition(), getAngle());
  }

  public abstract void setCoast(boolean value);

  public abstract void disableAccelerationLimiting();

  public abstract void enableAccelerationLimiting();
}
