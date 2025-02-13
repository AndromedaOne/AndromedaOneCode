// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import frc.robot.Config4905;

/** Add your docs here. */
public enum ArmSetpoints {
  CLIMBER_POSITION(Config4905.getConfig4905().getSBSDArmConfig().getDouble("climberPosition"),
      Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("climberPosition")),
  CORAL_LOAD(Config4905.getConfig4905().getSBSDArmConfig().getDouble("coralLoad"),
      Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("coralLoad")),
  LEVEL_1(Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_1"),
      Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("level_1")),
  LEVEL_2(Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_2"),
      Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("level_2")),
  LEVEL_3(Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_3"),
      Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("level_3")),
  LEVEL_4(Config4905.getConfig4905().getSBSDArmConfig().getDouble("level_4"),
      Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("level_4"));

  private final double angleInDeg;
  private final double endEffectorAngleInDeg;

  ArmSetpoints(double angleInDeg, double endEffectorAngleInDeg) {
    this.angleInDeg = angleInDeg;
    this.endEffectorAngleInDeg = endEffectorAngleInDeg;
  }

  public double getAngleInDeg() {
    return angleInDeg;
  }

  public double getEndEffectorAngleInDeg() {
    return endEffectorAngleInDeg;
  }
}
