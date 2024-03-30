// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.groupCommands.DelayedSequentialCommandGroup;
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
import frc.robot.telemetries.Trace;
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

    public String toString() {
      String str = new String("\twaypt 1: " + m_waypoint1 + "\n\tang 1: " + m_angle1
          + "\n\twaypt 2:" + m_waypoint2 + "\n\tang 2: " + m_angle2 + "\n\twaypt 3: " + m_waypoint3
          + "\n\tang 3: " + m_angle3 + "\n");
      return str;
    }
  }

  CentralSpeaker3ScoresConfig centralSpeaker3ScoresConfigRed = new CentralSpeaker3ScoresConfig();
  CentralSpeaker3ScoresConfig centralSpeaker3ScoresConfigBlue = new CentralSpeaker3ScoresConfig();
  CentralSpeaker3ScoresConfigSupplier m_configSupplier = new CentralSpeaker3ScoresConfigSupplier();
  DriveTrainBase m_driveTrain;
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;
  BillFeederBase m_feeder;
  BillShooterBase m_shooter;

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
    m_endEffector = subsystemsContainer.getBillEffectorPosition();
    m_armRotate = subsystemsContainer.getBillArmRotate();
    m_feeder = subsystemsContainer.getBillFeeder();
    m_shooter = subsystemsContainer.getBillShooter();
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
    m_configSupplier.setConfig(centralSpeaker3ScoresConfigRed);
    addCommands(new DelayedSequentialCommandGroup(
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE),
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint1, 1),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder, m_shooter, false)),
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.AWAY),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint2, 1),
        new TurnToCompassHeading(() -> m_configSupplier.getConfig().m_angle1),
        new PauseRobot(40, m_driveTrain),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint3, 1),
        new TurnToCompassHeading(() -> m_configSupplier.getConfig().m_angle2),
        new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint4, 1),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder, m_shooter, false)),
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.AWAY)

    ));
  }

  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      m_configSupplier.setConfig(centralSpeaker3ScoresConfigBlue);
    } else {
      m_configSupplier.setConfig(centralSpeaker3ScoresConfigRed);
    }
    Trace.getInstance().logCommandInfo(this, m_configSupplier.getConfig().toString());
  }

  private class CentralSpeaker3ScoresConfigSupplier {
    CentralSpeaker3ScoresConfig m_config;

    public void setConfig(CentralSpeaker3ScoresConfig config) {
      m_config = config;
    }

    public CentralSpeaker3ScoresConfig getConfig() {
      return m_config;
    }
  }
}
