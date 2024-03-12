// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.groupCommands.DelayedSequentialCommandGroup;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.BillSpeakerScore;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.DrivePositionCommand;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.IntakeNote;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.gyro.Gyro4905;
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
    double m_gyroOffset;
  }

  DriveStation3SpeakerWithAmpConfig driveStation3SpeakerWithAmpConfigRed = new DriveStation3SpeakerWithAmpConfig();
  DriveStation3SpeakerWithAmpConfig driveStation3SpeakerWithAmpConfigBlue = new DriveStation3SpeakerWithAmpConfig();
  DriveStation3SpeakerWithAmpConfigSupplier m_configSupplier = new DriveStation3SpeakerWithAmpConfigSupplier();
  DriveTrainBase m_driveTrain;
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;
  BillFeederBase m_feeder;
  BillShooterBase m_shooter;
  Gyro4905 m_gyro;

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
    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
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
    driveStation3SpeakerWithAmpConfigRed.m_gyroOffset = redConfig
        .getDouble("DriveStation3SpeakerWithAmp.GyroOffset");

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
    driveStation3SpeakerWithAmpConfigBlue.m_gyroOffset = blueConfig
        .getDouble("DriveStation3SpeakerWithAmp.GyroOffset");
    m_configSupplier.setConfig(driveStation3SpeakerWithAmpConfigRed);

    addCommands(new DelayedSequentialCommandGroup(
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint1, 1),
        new TurnToCompassHeading(m_configSupplier.getConfig().m_angle1),
        new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint2, 1),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder)),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint3, 1),
        new TurnToCompassHeading(m_configSupplier.getConfig().m_angle2),
        new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(new ArmRotate(m_armRotate, () -> 300, true),
            new MoveEndEffector(m_endEffector, () -> true)),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint4, 1),
        new RunBillFeeder(m_feeder, FeederStates.AMPSHOOTING),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint5, 1),
        new TurnToCompassHeading(m_configSupplier.getConfig().m_angle3),
        new PauseRobot(40, m_driveTrain), new DrivePositionCommand(m_endEffector, m_armRotate),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint6, 1)));
  }

  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      m_configSupplier.setConfig(driveStation3SpeakerWithAmpConfigBlue);
    } else {
      m_configSupplier.setConfig(driveStation3SpeakerWithAmpConfigRed);
    }
    m_gyro.setInitialZangleOffset(m_configSupplier.getConfig().m_gyroOffset, true);
    Trace.getInstance().logCommandInfo(this,
        "Setting off set to " + m_configSupplier.getConfig().m_gyroOffset);
  }

  private class DriveStation3SpeakerWithAmpConfigSupplier {
    DriveStation3SpeakerWithAmpConfig m_config;

    public void setConfig(DriveStation3SpeakerWithAmpConfig config) {
      m_config = config;
    }

    public DriveStation3SpeakerWithAmpConfig getConfig() {
      return m_config;
    }
  }
}
