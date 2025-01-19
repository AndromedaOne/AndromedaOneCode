// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators.SwerveModule;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

/** Add your docs here. */
public abstract class SwerveModuleBase {

  private int m_moduleNumber = 0;

  public SwerveModuleBase(int moduleNumber) {
    m_moduleNumber = moduleNumber;
  }

  public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop,
      boolean overRide) {
    desiredState.optimize(getAngle());
    setAngle(desiredState, overRide);
    setSpeed(desiredState, isOpenLoop);
  }

  protected abstract void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop);

  protected abstract void setAngle(SwerveModuleState desiredState, boolean overRide);

  public int getModuleNumber() {
    return m_moduleNumber;
  }

  protected abstract double getAngleMotorRawAngle();

  public Rotation2d getAngle() {
    return Rotation2d.fromDegrees(getAngleMotorRawAngle());
  }

  public abstract double getDriveEncoderPosition();

  public abstract double getDriveEncoderVelocity();

  public SwerveModuleState getState() {
    return new SwerveModuleState(getDriveEncoderPosition(), getAngle());
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(getDriveEncoderPosition(), getAngle());
  }

  public abstract void setCoast(boolean value);

  public abstract void disableAccelerationLimiting();

  public abstract void enableAccelerationLimiting();
}
