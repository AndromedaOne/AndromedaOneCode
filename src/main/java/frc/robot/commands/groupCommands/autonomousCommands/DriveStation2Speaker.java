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
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillSpeakerScore;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.IntakeNote;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;
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
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;
  BillFeederBase m_feeder;
  BillShooterBase m_shooter;

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
    m_endEffector = subsystemsContainer.getBillEffectorPosition();
    m_armRotate = subsystemsContainer.getBillArmRotate();
    m_feeder = subsystemsContainer.getBillFeeder();
    m_shooter = subsystemsContainer.getBillShooter();
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
    CommandScheduler.getInstance()
        .schedule(new SequentialCommandGroup4905(
            new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
                BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE),
            new MoveUsingEncoder(m_driveTrain, config.m_waypoint1, 1),
            new TurnToCompassHeading(config.m_angle1), new PauseRobot(40, m_driveTrain),
            new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint2, 1),
                new IntakeNote(m_armRotate, m_endEffector, m_feeder)),
            new TurnToCompassHeading(config.m_angle2), new PauseRobot(40, m_driveTrain),
            new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
                BillSpeakerScore.SpeakerScoreDistanceEnum.MID),
            new MoveUsingEncoder(m_driveTrain, config.m_waypoint3, 1)));
  }
}
