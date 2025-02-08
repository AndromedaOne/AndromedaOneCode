// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.teleOpPathCommands.PlaceAtA;
import frc.robot.oi.SubsystemController;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.utils.PoseEstimation4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class teleOpCoralScoring extends SequentialCommandGroup4905 {
  DriveTrainBase m_driveTrain;
  SubsystemController m_subsystemController;

  public enum reefScoringSide {
    LEFT, RIGHT, NOTSELECTED
  }

  /** Creates a new teleOpCoralScoring. */
  public teleOpCoralScoring(DriveTrainBase driveTrain) {
    m_driveTrain = driveTrain;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
    // gets region from drivetrain
    PoseEstimation4905.RegionsForPose currentRegion;
    currentRegion = m_driveTrain.getRegion();
    // gets axby button from subsystem controller for coral level
    int level = 0;
    if (m_subsystemController.getScoreLevelOne().getAsBoolean()) {
      level = 1;
    } else if (m_subsystemController.getScoreLevelTwo().getAsBoolean()) {
      level = 2;
    } else if (m_subsystemController.getScoreLevelThree().getAsBoolean()) {
      level = 3;
    } else if (m_subsystemController.getScoreLevelFour().getAsBoolean()) {
      level = 4;
    }
    // gets left/right bumper from subsystem controller for left or right placement
    reefScoringSide scoringSide = reefScoringSide.NOTSELECTED;
    if (m_subsystemController.getScoreRight().getAsBoolean()) {
      scoringSide = reefScoringSide.RIGHT;
    } else if (m_subsystemController.getScoreLeft().getAsBoolean()) {
      scoringSide = reefScoringSide.LEFT;
    }
    SmartDashboard.putNumber("scoring level", level);
    SmartDashboard.putString("Scoring Side", scoringSide.toString());

    // cancels if button not pressed
    if (level == 0 || scoringSide == reefScoringSide.NOTSELECTED) {
      return;
    }
    // figures out which path to run
    if (level == 4) {
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTH) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTH) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }

    } else {
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTH) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTH) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {

        } else {

        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {
  }
}
