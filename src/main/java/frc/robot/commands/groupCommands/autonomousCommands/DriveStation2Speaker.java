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
public class DriveStation2Speaker extends SequentialCommandGroup4905 {
  private class DriveStation2SpeakerConfig {
    double m_waypoint1;
    double m_angle1;
    double m_waypoint2;
    double m_angle2;
    double m_waypoint3;
  }

  DriveStation2SpeakerConfig driveStation2SpeakerConfigRed = new DriveStation2SpeakerConfig();
  DriveStation2SpeakerConfig driveStation2SpeakerConfigBlue = new DriveStation2SpeakerConfig();
  DriveTrainBase m_driveTrain;

  public DriveStation2Speaker() {
    // Both
    // Angled towards speaker by driver station 2
    // Score preloaded note in speaker
    // Red instructions
    // Drive foward towards R3 note
    // Turn Right and pick up the R3 note
    // Turn left to angle towards the speaker and score the note
    // Drive left and then forward to centerline without crossing
    // Blue instructions
    // Drive forward towards B3 note
    // Turn left and pick up B3 note
    // Turn Right to angle towards the speaker and score the note
    // Drive right and then forward to centerline without crossing
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    driveStation2SpeakerConfigRed.m_waypoint1 = redConfig
        .getDouble("DriveStation2Speaker.WayPoint1");
    driveStation2SpeakerConfigRed.m_angle1 = redConfig.getDouble("DriveStation2Speaker.Angle1");
    driveStation2SpeakerConfigRed.m_waypoint2 = redConfig
        .getDouble("DriveStation2Speaker.WayPoint2");
    driveStation2SpeakerConfigRed.m_angle2 = redConfig.getDouble("DriveStation2Speaker.Angle2");
    driveStation2SpeakerConfigRed.m_waypoint3 = redConfig
        .getDouble("DriveStation2Speaker.WayPoint3");

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    driveStation2SpeakerConfigBlue.m_waypoint1 = blueConfig
        .getDouble("DriveStation2Speaker.WayPoint1");
    driveStation2SpeakerConfigBlue.m_angle1 = blueConfig.getDouble("DriveStation2Speaker.Angle1");
    driveStation2SpeakerConfigBlue.m_waypoint2 = blueConfig
        .getDouble("DriveStation2Speaker.WayPoint2");
    driveStation2SpeakerConfigBlue.m_angle2 = blueConfig.getDouble("DriveStation2Speaker.Angle2");
    driveStation2SpeakerConfigBlue.m_waypoint3 = blueConfig
        .getDouble("DriveStation2Speaker.WayPoint3");

  }

  public void additionalInitialize() {
    DriveStation2SpeakerConfig config;
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      config = driveStation2SpeakerConfigBlue;
    } else {
      config = driveStation2SpeakerConfigRed;
    }
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup4905(
        // need shoot command
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint1, 0.5),
        new TurnToCompassHeading(config.m_angle1), new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint2, 0.5)// ,need
                                                                                                // intake
        // command
        ), new TurnToCompassHeading(config.m_angle2), new PauseRobot(40, m_driveTrain),
        // need shoot command
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint3, 0.5)));
  }
}
