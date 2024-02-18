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
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillAmpScore;
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
public class DriveStation3SpeakerWithAmp extends SequentialCommandGroup4905 {
  private class DriveStation3SpeakerWithAmpConfig {
    double m_waypoint1;
    double m_angle1;
    double m_waypoint2;
    double m_waypoint3;
    double m_angle2;
    double m_waypoint4;
    double m_waypoint5;
    double m_angle3;
    double m_waypoint6;
  }

  DriveStation3SpeakerWithAmpConfig driveStation3SpeakerWithAmpConfigRed = new DriveStation3SpeakerWithAmpConfig();
  DriveStation3SpeakerWithAmpConfig driveStation3SpeakerWithAmpConfigBlue = new DriveStation3SpeakerWithAmpConfig();
  DriveTrainBase m_driveTrain;
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;
  BillFeederBase m_feeder;
  BillShooterBase m_shooter;

  public DriveStation3SpeakerWithAmp() {
    // List of what this auto mode should do.
    // Both
    // 1. Positioned by drive station 3 and angeled towards the speaker in order to
    // score
    // 2. Shoot and score the preloaded note into the speaker
    // Blue Intructions
    // 3. drive forward and pick up the note B1
    // 4. drive diagonally left towards the amp
    // 5. Angle towards the amp and score the note into the amp
    // Red Instructions
    // 3. drive forward and pick up the note R1
    // 4. drive diagonally right towards the amp
    // 5. Angle towards the amp and score the note into the amp
    // Both
    // 6. drive forward towards the centerline
    // 7. Pick up the note C1
    // 8. Start driving back to score the note in Teleop
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    m_endEffector = subsystemsContainer.getBillEffectorPosition();
    m_armRotate = subsystemsContainer.getBillArmRotate();
    m_feeder = subsystemsContainer.getBillFeeder();
    m_shooter = subsystemsContainer.getBillShooter();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    driveStation3SpeakerWithAmpConfigRed.m_waypoint1 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint1");
    driveStation3SpeakerWithAmpConfigRed.m_angle1 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.Angle1");
    driveStation3SpeakerWithAmpConfigRed.m_waypoint2 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint2");
    driveStation3SpeakerWithAmpConfigRed.m_waypoint3 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint3");
    driveStation3SpeakerWithAmpConfigRed.m_angle2 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.Angle2");
    driveStation3SpeakerWithAmpConfigRed.m_waypoint4 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint4");
    driveStation3SpeakerWithAmpConfigRed.m_waypoint5 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint5");
    driveStation3SpeakerWithAmpConfigRed.m_angle3 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.Angle3");
    driveStation3SpeakerWithAmpConfigRed.m_waypoint6 = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint6");

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    driveStation3SpeakerWithAmpConfigBlue.m_waypoint1 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint1");
    driveStation3SpeakerWithAmpConfigBlue.m_angle1 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.Angle1");
    driveStation3SpeakerWithAmpConfigBlue.m_waypoint2 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint2");
    driveStation3SpeakerWithAmpConfigBlue.m_waypoint3 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint3");
    driveStation3SpeakerWithAmpConfigBlue.m_angle2 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.Angle2");
    driveStation3SpeakerWithAmpConfigBlue.m_waypoint4 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint4");
    driveStation3SpeakerWithAmpConfigBlue.m_waypoint5 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint5");
    driveStation3SpeakerWithAmpConfigBlue.m_angle3 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.Angle3");
    driveStation3SpeakerWithAmpConfigBlue.m_waypoint6 = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.WayPoint6");

  }

  public void additionalInitialize() {
    DriveStation3SpeakerWithAmpConfig config;
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      config = driveStation3SpeakerWithAmpConfigBlue;
    } else {
      config = driveStation3SpeakerWithAmpConfigRed;
    }
    CommandScheduler.getInstance().schedule(new SequentialCommandGroup4905(
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE),
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint1, 0.5),
        new TurnToCompassHeading(config.m_angle1), new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint2, 0.5),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder)),
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint3, 0.5),
        new TurnToCompassHeading(config.m_angle2), new PauseRobot(40, m_driveTrain),
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint4, 0.5),
        new BillAmpScore(m_armRotate, m_endEffector, m_feeder, m_shooter),
        new MoveUsingEncoder(m_driveTrain, config.m_waypoint5, 0.5),
        new TurnToCompassHeading(config.m_angle3), new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint6, 0.5),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder))));
  }
}
