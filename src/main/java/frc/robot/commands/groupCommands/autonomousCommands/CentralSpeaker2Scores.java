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
public class CentralSpeaker2Scores extends SequentialCommandGroup4905 {
  private class CentralSpeaker2ScoresConfig {
    double m_waypoint1;
    double m_angle1;
    double m_waypoint2;
    double m_angle2;
    double m_waypoint3;
  }

  CentralSpeaker2ScoresConfig CentralSpeaker2ScoresConfigRed = new CentralSpeaker2ScoresConfig();
  CentralSpeaker2ScoresConfig CentralSpeaker2ScoresConfigBlue = new CentralSpeaker2ScoresConfig();
  CentralSpeaker2ScoresConfigSupplier m_configSupplier;
  DriveTrainBase m_driveTrain;
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;
  BillFeederBase m_feeder;
  BillShooterBase m_shooter;

  public CentralSpeaker2Scores() {
    // List of what this auto mode should do:
    // Both
    // 1. Positioned at Central Speaker.
    // 2. Shoot and score the preloaded note into the speaker.
    // 3. drive forward.
    // Red instructions
    // 4. drive forward and pick up the R2 Note
    // Blue instructions
    // 4. drive forward and pick up the B2 Note
    // 5. Drive Backwards if necessary to shoot the note into the speaker.
    // 6. Shoot and score the note into the speaker.
    // Red instructions
    // 7. Drive forward and then diagonally right and then diagonally left to
    // go around stage to get to C2 Note
    // Blue instructions
    // 7. Drive forward and then diagonally left and then diagonally right to
    // go around stage to get to C2 Note
    // Both
    // 8. Pick up note C2
    // 9. Start driving backward towards speaker so the robot can score the note in
    // Teleop.
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    m_endEffector = subsystemsContainer.getBillEffectorPosition();
    m_armRotate = subsystemsContainer.getBillArmRotate();
    m_feeder = subsystemsContainer.getBillFeeder();
    m_shooter = subsystemsContainer.getBillShooter();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    CentralSpeaker2ScoresConfigRed.m_waypoint1 = redConfig
        .getDouble("CentralSpeaker2Scores.WayPoint1");
    CentralSpeaker2ScoresConfigRed.m_angle1 = redConfig.getDouble("CentralSpeaker2Scores.Angle1");
    CentralSpeaker2ScoresConfigRed.m_waypoint2 = redConfig
        .getDouble("CentralSpeaker2Scores.WayPoint2");
    CentralSpeaker2ScoresConfigRed.m_angle2 = redConfig.getDouble("CentralSpeaker2Scores.Angle2");
    CentralSpeaker2ScoresConfigRed.m_waypoint3 = redConfig
        .getDouble("CentralSpeaker2Scores.WayPoint3");

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    CentralSpeaker2ScoresConfigBlue.m_waypoint1 = blueConfig
        .getDouble("CentralSpeaker2Scores.WayPoint1");
    CentralSpeaker2ScoresConfigBlue.m_angle1 = blueConfig.getDouble("CentralSpeaker2Scores.Angle1");
    CentralSpeaker2ScoresConfigBlue.m_waypoint2 = blueConfig
        .getDouble("CentralSpeaker2Scores.WayPoint2");
    CentralSpeaker2ScoresConfigBlue.m_angle2 = blueConfig.getDouble("CentralSpeaker2Scores.Angle2");
    CentralSpeaker2ScoresConfigBlue.m_waypoint3 = blueConfig
        .getDouble("CentralSpeaker2Scores.WayPoint3");

    addCommands(
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE),
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint1, 1),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder)),
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.MID),
        new TurnToCompassHeading(() -> m_configSupplier.getConfig().m_angle1),
        new PauseRobot(40, m_driveTrain),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint2, 1),
        new TurnToCompassHeading(() -> m_configSupplier.getConfig().m_angle2),
        new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint3, 1),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder)));
  }

  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      m_configSupplier.setConfig(CentralSpeaker2ScoresConfigBlue);
    } else {
      m_configSupplier.setConfig(CentralSpeaker2ScoresConfigRed);
    }

  }

  private class CentralSpeaker2ScoresConfigSupplier {
    CentralSpeaker2ScoresConfig m_config;

    public void setConfig(CentralSpeaker2ScoresConfig config) {
      m_config = config;
    }

    public CentralSpeaker2ScoresConfig getConfig() {
      return m_config;
    }
  }
}
