// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import frc.robot.commands.teleOpPathCommands.GenericCoralPickUpCommand;
import frc.robot.commands.teleOpPathCommands.LeftWallCoralStation;
import frc.robot.commands.teleOpPathCommands.RightWallCoralStation;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class teleOpWallCoralPickup extends SequentialCommandGroup4905 {
  DriveTrainBase m_driveTrain;
  GenericCoralPickUpCommand m_leftWallCoralStation;
  GenericCoralPickUpCommand m_rightWallCoralStation;

  /** Creates a new teleOpDriverCoralPickup. */
  public teleOpWallCoralPickup(DriveTrainBase driveTrain) {
    m_driveTrain = driveTrain;
    try {
      m_leftWallCoralStation = new GenericCoralPickUpCommand(new LeftWallCoralStation(),
          new sbsdCoralLoadArmEndEffectorPositon());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_rightWallCoralStation = new GenericCoralPickUpCommand(new RightWallCoralStation(),
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
      m_leftWallCoralStation.schedule();
    } else {
      m_rightWallCoralStation.schedule();
    }
  }
}
