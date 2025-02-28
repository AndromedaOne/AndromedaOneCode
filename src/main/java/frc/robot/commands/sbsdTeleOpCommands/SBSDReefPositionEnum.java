// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

/** Add your docs here. */
public enum SBSDReefPositionEnum {
  A(new Pose2d(3.17, 4.19, Rotation2d.fromDegrees(0))),
  B(new Pose2d(3.17, 3.875, Rotation2d.fromDegrees(0))),
  C(new Pose2d(3.711, 2.958, Rotation2d.fromDegrees(60))),
  D(new Pose2d(3.967, 2.793, Rotation2d.fromDegrees(60))),
  E(new Pose2d(4.989, 2.793, Rotation2d.fromDegrees(120))),
  F(new Pose2d(5.259, 2.943, Rotation2d.fromDegrees(120))),
  G(new Pose2d(5.815, 3.845, Rotation2d.fromDegrees(180))),
  H(new Pose2d(5.815, 4.205, Rotation2d.fromDegrees(180))),
  I(new Pose2d(5.292, 5.09, Rotation2d.fromDegrees(240))),
  J(new Pose2d(5.01, 5.25, Rotation2d.fromDegrees(240))),
  K(new Pose2d(3.988, 5.269, Rotation2d.fromDegrees(300))),
  L(new Pose2d(3.698, 5.11, Rotation2d.fromDegrees(300))),
  A4(new Pose2d(3.17, 4.19, Rotation2d.fromDegrees(180))),
  B4(new Pose2d(3.17, 3.875, Rotation2d.fromDegrees(180))),
  C4(new Pose2d(3.711, 2.958, Rotation2d.fromDegrees(240))),
  D4(new Pose2d(3.967, 2.793, Rotation2d.fromDegrees(240))),
  E4(new Pose2d(4.989, 2.793, Rotation2d.fromDegrees(300))),
  F4(new Pose2d(5.259, 2.943, Rotation2d.fromDegrees(300))),
  G4(new Pose2d(5.815, 3.845, Rotation2d.fromDegrees(0))),
  H4(new Pose2d(5.815, 4.205, Rotation2d.fromDegrees(0))),
  I4(new Pose2d(5.292, 5.09, Rotation2d.fromDegrees(60))),
  J4(new Pose2d(5.01, 5.25, Rotation2d.fromDegrees(60))),
  K4(new Pose2d(3.988, 5.269, Rotation2d.fromDegrees(120))),
  L4(new Pose2d(3.698, 5.11, Rotation2d.fromDegrees(120)));

  private final Pose2d pose;

  SBSDReefPositionEnum(Pose2d pose) {
    this.pose = pose;
  }

  public Pose2d getReefPosition() {
    return pose;
  }
}