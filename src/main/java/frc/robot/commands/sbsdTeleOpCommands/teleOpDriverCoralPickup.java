// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import frc.robot.commands.driveTrainCommands.SwerveDriveSetVelocityToZero;
import frc.robot.commands.teleOpPathCommands.GenericCoralPickUpCommand;
import frc.robot.commands.teleOpPathCommands.LeftDriverCoralStation;
import frc.robot.commands.teleOpPathCommands.RightDriverCoralStation;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class teleOpDriverCoralPickup extends SequentialCommandGroup4905 {
  DriveTrainBase m_driveTrain;
  GenericCoralPickUpCommand m_leftDriverCoralStation;
  GenericCoralPickUpCommand m_rightDriverCoralStation;

  /** Creates a new teleOpDriverCoralPickup. */
  public teleOpDriverCoralPickup(DriveTrainBase driveTrain) {
    m_driveTrain = driveTrain;
    try {
      m_leftDriverCoralStation = new GenericCoralPickUpCommand(
          new LeftDriverCoralStation().andThen(new SwerveDriveSetVelocityToZero(driveTrain)),
          new sbsdCoralLoadArmEndEffectorPositon());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_rightDriverCoralStation = new GenericCoralPickUpCommand(
          new RightDriverCoralStation().andThen(new SwerveDriveSetVelocityToZero(driveTrain)),
          new sbsdCoralLoadArmEndEffectorPositon());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void additionalInitialize() {
    // gets side from drivetrain
    boolean isLeftSide = m_driveTrain.isLeftSide();
    // chooses coral station
    if (isLeftSide) {
      m_leftDriverCoralStation.schedule();
    } else {
      m_rightDriverCoralStation.schedule();
    }
  }
}
