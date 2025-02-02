// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.armTestBedCommands;

/** Add your docs here. */
public enum ArmSetpoints {
  CORAL_LOAD(-42), LEVEL_1(-25), LEVEL_2(-8), LEVEL_3(20), LEVEL_4(95);

  private final double angleInDeg;

  ArmSetpoints(double angleInDeg) {
    this.angleInDeg = angleInDeg;
  }

  public double getAngleInDeg() {
    return angleInDeg;
  }
}
