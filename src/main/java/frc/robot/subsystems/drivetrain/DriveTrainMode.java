// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivetrain;

/** Add your docs here. */
public class DriveTrainMode {
  public enum DriveTrainModeEnum {
    FAST, MID, SLOW;
  }

  private DriveTrainModeEnum m_driveTrainMode = DriveTrainModeEnum.FAST;

  public void setDriveTrainMode(DriveTrainModeEnum mode) {
    m_driveTrainMode = mode;
  }

  public DriveTrainModeEnum getDriveTrainMode() {
    return m_driveTrainMode;
  }
}
