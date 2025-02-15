// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.commands.teleOpPathCommands.GenericCoralPathCommand;
import frc.robot.commands.teleOpPathCommands.PlaceAtA;
import frc.robot.commands.teleOpPathCommands.PlaceAtA4;
import frc.robot.commands.teleOpPathCommands.PlaceAtB;
import frc.robot.commands.teleOpPathCommands.PlaceAtB4;
import frc.robot.commands.teleOpPathCommands.PlaceAtC;
import frc.robot.commands.teleOpPathCommands.PlaceAtC4;
import frc.robot.commands.teleOpPathCommands.PlaceAtD;
import frc.robot.commands.teleOpPathCommands.PlaceAtD4;
import frc.robot.commands.teleOpPathCommands.PlaceAtE;
import frc.robot.commands.teleOpPathCommands.PlaceAtE4;
import frc.robot.commands.teleOpPathCommands.PlaceAtF;
import frc.robot.commands.teleOpPathCommands.PlaceAtF4;
import frc.robot.commands.teleOpPathCommands.PlaceAtG;
import frc.robot.commands.teleOpPathCommands.PlaceAtG4;
import frc.robot.commands.teleOpPathCommands.PlaceAtH;
import frc.robot.commands.teleOpPathCommands.PlaceAtH4;
import frc.robot.commands.teleOpPathCommands.PlaceAtI;
import frc.robot.commands.teleOpPathCommands.PlaceAtI4;
import frc.robot.commands.teleOpPathCommands.PlaceAtJ;
import frc.robot.commands.teleOpPathCommands.PlaceAtJ4;
import frc.robot.commands.teleOpPathCommands.PlaceAtK;
import frc.robot.commands.teleOpPathCommands.PlaceAtK4;
import frc.robot.commands.teleOpPathCommands.PlaceAtL;
import frc.robot.commands.teleOpPathCommands.PlaceAtL4;
import frc.robot.oi.SubsystemController;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.utils.PoseEstimation4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class teleOpCoralScoring extends SequentialCommandGroup4905 {
  DriveTrainBase m_driveTrain;
  SubsystemController m_subsystemController;
  GenericCoralPathCommand m_placeAtA;
  GenericCoralPathCommand m_placeAtB;
  GenericCoralPathCommand m_placeAtC;
  GenericCoralPathCommand m_placeAtD;
  GenericCoralPathCommand m_placeAtE;
  GenericCoralPathCommand m_placeAtF;
  GenericCoralPathCommand m_placeAtG;
  GenericCoralPathCommand m_placeAtH;
  GenericCoralPathCommand m_placeAtI;
  GenericCoralPathCommand m_placeAtJ;
  GenericCoralPathCommand m_placeAtK;
  GenericCoralPathCommand m_placeAtL;
  GenericCoralPathCommand m_placeAtA4;
  GenericCoralPathCommand m_placeAtB4;
  GenericCoralPathCommand m_placeAtC4;
  GenericCoralPathCommand m_placeAtD4;
  GenericCoralPathCommand m_placeAtE4;
  GenericCoralPathCommand m_placeAtF4;
  GenericCoralPathCommand m_placeAtG4;
  GenericCoralPathCommand m_placeAtH4;
  GenericCoralPathCommand m_placeAtI4;
  GenericCoralPathCommand m_placeAtJ4;
  GenericCoralPathCommand m_placeAtK4;
  GenericCoralPathCommand m_placeAtL4;
  int m_level;

  public enum reefScoringSide {
    LEFT, RIGHT, NOTSELECTED
  }

  /** Creates a new teleOpCoralScoring. */
  public teleOpCoralScoring(DriveTrainBase driveTrain) {
    m_driveTrain = driveTrain;
    try {
      m_placeAtA = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtA(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtB = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtB(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtC = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtC(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtD = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtD(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtE = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtE(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtF = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtF(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtG = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtG(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtH = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtH(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtI = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtI(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtJ = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtJ(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtK = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtK(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtL = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtL(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtA4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtA4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtB4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtB4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtC4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtC4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtD4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtD4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtE4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtE4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtF4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtF4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtG4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtG4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtH4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtH4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtI4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtI4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtJ4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtJ4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtK4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtK4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtL4 = new GenericCoralPathCommand(new ParallelCommandGroup4905(new PlaceAtL4(),
          new sbsdMoveArmAndEndEffector(() -> m_level)), new sbsdScoreCoral());
    } catch (Exception e) {
      e.printStackTrace();
    }
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
    if (m_subsystemController.getScoreLevelOne().getAsBoolean()) {
      m_level = 1;
    } else if (m_subsystemController.getScoreLevelTwo().getAsBoolean()) {
      m_level = 2;
    } else if (m_subsystemController.getScoreLevelThree().getAsBoolean()) {
      m_level = 3;
    } else if (m_subsystemController.getScoreLevelFour().getAsBoolean()) {
      m_level = 4;
    }

    // gets left/right bumper from subsystem controller for left or right placement
    reefScoringSide scoringSide = reefScoringSide.NOTSELECTED;
    if (m_subsystemController.getScoreRight().getAsBoolean()) {
      scoringSide = reefScoringSide.RIGHT;
    } else if (m_subsystemController.getScoreLeft().getAsBoolean()) {
      scoringSide = reefScoringSide.LEFT;
    }
    SmartDashboard.putNumber("scoring level", m_level);
    SmartDashboard.putString("Scoring Side", scoringSide.toString());

    // cancels if button not pressed
    if (m_level == 0 || scoringSide == reefScoringSide.NOTSELECTED) {
      return;
    }
    // figures out which path to run
    if (m_level == 4) {
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTH) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtA4.schedule();
        } else {
          m_placeAtB4.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtC4.schedule();
        } else {
          m_placeAtD4.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtE4.schedule();
        } else {
          m_placeAtF4.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTH) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtG4.schedule();
        } else {
          m_placeAtH4.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtI4.schedule();
        } else {
          m_placeAtJ4.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtK4.schedule();
        } else {
          m_placeAtL4.schedule();
        }
      }

    } else {
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTH) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtA.schedule();
        } else {
          m_placeAtB.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtC.schedule();
        } else {
          m_placeAtD.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHEAST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtE.schedule();
        } else {
          m_placeAtF.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTH) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtG.schedule();
        } else {
          m_placeAtH.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.NORTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtI.schedule();
        } else {
          m_placeAtJ.schedule();
        }
      }
      if (currentRegion == PoseEstimation4905.RegionsForPose.SOUTHWEST) {
        if (scoringSide == reefScoringSide.LEFT) {
          m_placeAtK.schedule();
        } else {
          m_placeAtL.schedule();
        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {
  }
}
