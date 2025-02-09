// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
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
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.utils.PoseEstimation4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class teleOpCoralScoring extends SequentialCommandGroup4905 {
  DriveTrainBase m_driveTrain;
  SubsystemController m_subsystemController;
  SequentialCommandGroup4905 m_placeAtA;
  SequentialCommandGroup4905 m_placeAtB;
  SequentialCommandGroup4905 m_placeAtC;
  SequentialCommandGroup4905 m_placeAtD;
  SequentialCommandGroup4905 m_placeAtE;
  SequentialCommandGroup4905 m_placeAtF;
  SequentialCommandGroup4905 m_placeAtG;
  SequentialCommandGroup4905 m_placeAtH;
  SequentialCommandGroup4905 m_placeAtI;
  SequentialCommandGroup4905 m_placeAtJ;
  SequentialCommandGroup4905 m_placeAtK;
  SequentialCommandGroup4905 m_placeAtL;
  SequentialCommandGroup4905 m_placeAtA4;
  SequentialCommandGroup4905 m_placeAtB4;
  SequentialCommandGroup4905 m_placeAtC4;
  SequentialCommandGroup4905 m_placeAtD4;
  SequentialCommandGroup4905 m_placeAtE4;
  SequentialCommandGroup4905 m_placeAtF4;
  SequentialCommandGroup4905 m_placeAtG4;
  SequentialCommandGroup4905 m_placeAtH4;
  SequentialCommandGroup4905 m_placeAtI4;
  SequentialCommandGroup4905 m_placeAtJ4;
  SequentialCommandGroup4905 m_placeAtK4;
  SequentialCommandGroup4905 m_placeAtL4;
  int m_level;

  public enum reefScoringSide {
    LEFT, RIGHT, NOTSELECTED
  }

  /** Creates a new teleOpCoralScoring. */
  public teleOpCoralScoring(DriveTrainBase driveTrain) {
    m_driveTrain = driveTrain;
    try {
      m_placeAtA = new SequentialCommandGroup4905(new PlaceAtA());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtB = new SequentialCommandGroup4905(new PlaceAtB());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtC = new SequentialCommandGroup4905(new PlaceAtC());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtD = new SequentialCommandGroup4905(new PlaceAtD());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtE = new SequentialCommandGroup4905(new PlaceAtE());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtF = new SequentialCommandGroup4905(new PlaceAtF());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtG = new SequentialCommandGroup4905(new PlaceAtG());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtH = new SequentialCommandGroup4905(new PlaceAtH());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtI = new SequentialCommandGroup4905(new PlaceAtI());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtJ = new SequentialCommandGroup4905(new PlaceAtJ());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtK = new SequentialCommandGroup4905(new PlaceAtK());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtL = new SequentialCommandGroup4905(new PlaceAtL());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtA4 = new SequentialCommandGroup4905(new PlaceAtA4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtB4 = new SequentialCommandGroup4905(new PlaceAtB4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtC4 = new SequentialCommandGroup4905(new PlaceAtC4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtD4 = new SequentialCommandGroup4905(new PlaceAtD4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtE4 = new SequentialCommandGroup4905(new PlaceAtE4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtF4 = new SequentialCommandGroup4905(new PlaceAtF4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtG4 = new SequentialCommandGroup4905(new PlaceAtG4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtH4 = new SequentialCommandGroup4905(new PlaceAtH4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtI4 = new SequentialCommandGroup4905(new PlaceAtI4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtJ4 = new SequentialCommandGroup4905(new PlaceAtJ4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtK4 = new SequentialCommandGroup4905(new PlaceAtK4());
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      m_placeAtL4 = new SequentialCommandGroup4905(new PlaceAtL4());
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
