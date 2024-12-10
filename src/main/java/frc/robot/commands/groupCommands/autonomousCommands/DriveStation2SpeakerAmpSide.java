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
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.armTestBenchRotate.ArmTestBenchRotateBase;
import frc.robot.subsystems.billEndEffectorPosition.BillEndEffectorPositionBase;
import frc.robot.subsystems.billFeeder.BillFeederBase;
import frc.robot.subsystems.billShooter.BillShooterBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.AllianceConfig;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DriveStation2SpeakerAmpSide extends SequentialCommandGroup4905 {
  private class DriveStation2SpeakerConfig {
    double m_waypoint1;
    double m_angle1;
    double m_waypoint2;
    double m_gyroOffset;

    public String toString() {
      String str = new String("\twaypt 1: " + m_waypoint1 + "\n\tang 1: " + m_angle1
          + "\n\twaypt 2:" + m_waypoint2 + "\n\tgyrooffset: " + m_gyroOffset + "\n");
      return str;
    }
  }

  DriveStation2SpeakerConfig driveStation2SpeakerConfigRed = new DriveStation2SpeakerConfig();
  DriveStation2SpeakerConfig driveStation2SpeakerConfigBlue = new DriveStation2SpeakerConfig();
  DriveStation2SpeakerConfigSupplier m_configSupplier = new DriveStation2SpeakerConfigSupplier();
  DriveTrainBase m_driveTrain;
  BillEndEffectorPositionBase m_endEffector;
  ArmTestBenchRotateBase m_armRotate;
  BillFeederBase m_feeder;
  BillShooterBase m_shooter;
  Gyro4905 m_gyro;

  public DriveStation2SpeakerAmpSide() {
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
    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    driveStation2SpeakerConfigRed.m_waypoint1 = redConfig
        .getDouble("DriveStation2SpeakerAmpSide.WayPoint1");
    driveStation2SpeakerConfigRed.m_angle1 = redConfig
        .getDouble("DriveStation2SpeakerAmpSide.Angle1");
    driveStation2SpeakerConfigRed.m_waypoint2 = redConfig
        .getDouble("DriveStation2SpeakerAmpSide.WayPoint2");
    driveStation2SpeakerConfigRed.m_gyroOffset = redConfig
        .getDouble("DriveStation2SpeakerAmpSide.GyroOffset");
    System.out.println("Red gyroOffset: " + driveStation2SpeakerConfigRed.m_gyroOffset);

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    driveStation2SpeakerConfigBlue.m_waypoint1 = blueConfig
        .getDouble("DriveStation2SpeakerAmpSide.WayPoint1");
    driveStation2SpeakerConfigBlue.m_angle1 = blueConfig
        .getDouble("DriveStation2SpeakerAmpSide.Angle1");
    driveStation2SpeakerConfigBlue.m_waypoint2 = blueConfig
        .getDouble("DriveStation2SpeakerAmpSide.WayPoint2");
    driveStation2SpeakerConfigBlue.m_gyroOffset = blueConfig
        .getDouble("DriveStation2SpeakerAmpSide.GyroOffset");
    System.out.println("Blue gyroOffset: " + driveStation2SpeakerConfigBlue.m_gyroOffset);
    m_configSupplier.setConfig(driveStation2SpeakerConfigRed);
    addCommands(new DelayedSequentialCommandGroup(
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint1, 1),
        new TurnToCompassHeading(() -> m_configSupplier.getConfig().m_angle1),
        new PauseRobot(40, m_driveTrain),
        new ParallelDeadlineGroup4905(
            new IntakeNote(m_armRotate, m_endEffector, m_feeder, m_shooter, false),
            new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint2, 1)),

        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.AWAY)));
  }

  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      m_configSupplier.setConfig(driveStation2SpeakerConfigBlue);
    } else {
      m_configSupplier.setConfig(driveStation2SpeakerConfigRed);
    }
    m_gyro.setInitialZangleOffset(m_configSupplier.getConfig().m_gyroOffset, true);
    Trace.getInstance().logCommandInfo(this,
        "Setting offset to " + m_configSupplier.getConfig().m_gyroOffset);
    Trace.getInstance().logCommandInfo(this, m_configSupplier.getConfig().toString());

  }

  private class DriveStation2SpeakerConfigSupplier {
    DriveStation2SpeakerConfig m_config;

    public void setConfig(DriveStation2SpeakerConfig config) {
      m_config = config;
    }

    public DriveStation2SpeakerConfig getConfig() {
      return m_config;
    }
  }
}
