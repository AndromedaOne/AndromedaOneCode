// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.utils.AllianceConfig;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CentralSpeaker3Scores extends SequentialCommandGroup4905 {
  private class CentralSpeaker3ScoresConfig {
    double m_waypoint1;
    double m_waypoint2;
    double m_angle1;
    double m_waypoint3;
    double m_angle2;
    double m_waypoint4;
    double m_angle3;
  }

  CentralSpeaker3ScoresConfig centralSpeaker3ScoresConfigRed = new CentralSpeaker3ScoresConfig();
  CentralSpeaker3ScoresConfig centralSpeaker3ScoresConfigBlue = new CentralSpeaker3ScoresConfig();
  DriveTrainBase m_driveTrain;

  public CentralSpeaker3Scores() {
    // Positioned by the central speaker
    // Score preloaded note in speaker
    // drive foward toward Note B2/R2
    // pick up Note B2/R2
    // Score on speaker from point X (X=TBD)
    // drive foward and around the stage to B3/R3
    // pick up B3/R3
    // drive back towards the speaker
    // score 3rd Note on speaker from point Y (Y=TBD)
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    centralSpeaker3ScoresConfigRed.m_waypoint1 = redConfig
        .getDouble("CentralSpeaker3Scores.WayPoint1");
    centralSpeaker3ScoresConfigRed.m_waypoint2 = redConfig
        .getDouble("CentralSpeaker3Scores.WayPoint2");
    centralSpeaker3ScoresConfigRed.m_angle1 = redConfig.getDouble("CentralSpeaker3Scores.Angle1");
    centralSpeaker3ScoresConfigRed.m_waypoint3 = redConfig
        .getDouble("CentralSpeaker3Scores.WayPoint3");
    centralSpeaker3ScoresConfigRed.m_angle2 = redConfig.getDouble("CentralSpeaker3Scores.Angle2");
    centralSpeaker3ScoresConfigRed.m_waypoint4 = redConfig
        .getDouble("CentralSpeaker3Scores.WayPoint4");
    centralSpeaker3ScoresConfigRed.m_angle3 = redConfig.getDouble("CentralSpeaker3Scores.Angle3");

    Config blueConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    centralSpeaker3ScoresConfigBlue.m_waypoint1 = blueConfig
        .getDouble("CentralSpeaker3Scores.WayPoint1");
    centralSpeaker3ScoresConfigBlue.m_waypoint2 = blueConfig
        .getDouble("CentralSpeaker3Scores.WayPoint2");
    centralSpeaker3ScoresConfigBlue.m_angle1 = blueConfig.getDouble("CentralSpeaker3Scores.Angle1");
    centralSpeaker3ScoresConfigBlue.m_waypoint3 = blueConfig
        .getDouble("CentralSpeaker3Scores.WayPoint3");
    centralSpeaker3ScoresConfigBlue.m_angle2 = blueConfig.getDouble("CentralSpeaker3Scores.Angle2");
    centralSpeaker3ScoresConfigBlue.m_waypoint4 = blueConfig
        .getDouble("CentralSpeaker3Scores.WayPoint4");
    centralSpeaker3ScoresConfigBlue.m_angle3 = blueConfig.getDouble("CentralSpeaker3Scores.Angle3");

  }

  public void additionalInitialize() {
    CentralSpeaker3ScoresConfig config;
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      config = centralSpeaker3ScoresConfigBlue;
    } else {
      config = centralSpeaker3ScoresConfigRed;
    }
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup4905(
        // need shooter command
        new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint1, 0.5)// ,
                                                                                                // need
        // intake
        // command
        ),
        // need shooter command
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint2, 0.5),
        new TurnToCompassHeading(config.m_angle1), new PauseRobot(40, m_driveTrain),
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint3, 0.5),
        new TurnToCompassHeading(config.m_angle2), new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint4, 0.5)// ,
                                                                                                // need
        // intake
        // command
        ), new TurnToCompassHeading(config.m_angle3), new PauseRobot(40, m_driveTrain)
    // need shooter command

    ));
  }
}
