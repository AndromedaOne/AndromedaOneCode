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

public class SideScoreLeaveHome extends SequentialCommandGroup4905 {
  private class SideScoreLeaveHomeConfig {
    double m_waypoint1;
    double m_angle1;
    double m_waypoint2;
    double m_angle2;
    double m_waypoint3;
    double m_gyroOffset;
  }

  SideScoreLeaveHomeConfig SideScoreLeaveHomeConfigRed = new SideScoreLeaveHomeConfig();
  SideScoreLeaveHomeConfig SideScoreLeaveHomeConfigBlue = new SideScoreLeaveHomeConfig();
  SideScoreLeaveHomeConfigSupplier m_configSupplier = new SideScoreLeaveHomeConfigSupplier();
  DriveTrainBase m_driveTrain;
  BillEndEffectorPositionBase m_endEffector;
  BillArmRotateBase m_armRotate;
  BillFeederBase m_feeder;
  BillShooterBase m_shooter;
  Gyro4905 m_gyro;

  /** Creates a new SideScoreLeaveHome. */
  public SideScoreLeaveHome() {
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    m_endEffector = subsystemsContainer.getBillEffectorPosition();
    m_armRotate = subsystemsContainer.getBillArmRotate();
    m_feeder = subsystemsContainer.getBillFeeder();
    m_shooter = subsystemsContainer.getBillShooter();
    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();

    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    SideScoreLeaveHomeConfigRed.m_waypoint1 = redConfig.getDouble("SideScoreLeaveHome.WayPoint1");
    SideScoreLeaveHomeConfigRed.m_angle1 = redConfig.getDouble("SideScoreLeaveHome.Angle1");
    SideScoreLeaveHomeConfigRed.m_waypoint2 = redConfig.getDouble("SideScoreLeaveHome.WayPoint2");
    SideScoreLeaveHomeConfigRed.m_angle2 = redConfig.getDouble("SideScoreLeaveHome.Angle2");
    SideScoreLeaveHomeConfigRed.m_waypoint3 = redConfig.getDouble("SideScoreLeaveHome.WayPoint3");
    SideScoreLeaveHomeConfigRed.m_gyroOffset = redConfig.getDouble("SideScoreLeaveHome.GyroOffset");
    System.out.println("Red gyroOffset: " + SideScoreLeaveHomeConfigRed.m_gyroOffset);

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    SideScoreLeaveHomeConfigBlue.m_waypoint1 = blueConfig.getDouble("SideScoreLeaveHome.WayPoint1");
    SideScoreLeaveHomeConfigBlue.m_angle1 = blueConfig.getDouble("SideScoreLeaveHome.Angle1");
    SideScoreLeaveHomeConfigBlue.m_waypoint2 = blueConfig.getDouble("SideScoreLeaveHome.WayPoint2");
    SideScoreLeaveHomeConfigBlue.m_angle2 = blueConfig.getDouble("SideScoreLeaveHome.Angle2");
    SideScoreLeaveHomeConfigBlue.m_waypoint3 = blueConfig.getDouble("SideScoreLeaveHome.WayPoint3");
    SideScoreLeaveHomeConfigBlue.m_gyroOffset = blueConfig
        .getDouble("SideScoreLeaveHome.GyroOffset");
    System.out.println("Blue gyroOffset: " + SideScoreLeaveHomeConfigBlue.m_gyroOffset);
    m_configSupplier.setConfig(SideScoreLeaveHomeConfigRed);
    addCommands(new DelayedSequentialCommandGroup(
        new BillSpeakerScore(m_armRotate, m_endEffector, m_feeder, m_shooter,
            BillSpeakerScore.SpeakerScoreDistanceEnum.CLOSE),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint1, 1),
        new TurnToCompassHeading(m_configSupplier.getConfig().m_angle1),
        new PauseRobot(40, m_driveTrain),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint2, 1),
        new TurnToCompassHeading(m_configSupplier.getConfig().m_angle2),
        new PauseRobot(40, m_driveTrain),
        new MoveUsingEncoder(m_driveTrain, () -> m_configSupplier.getConfig().m_waypoint3, 1)));

    // Use addRequirements() here to declare subsystem dependencies.
  }

  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      m_configSupplier.setConfig(SideScoreLeaveHomeConfigBlue);
    } else {
      m_configSupplier.setConfig(SideScoreLeaveHomeConfigRed);
    }
    m_gyro.setInitialZangleOffset(m_configSupplier.getConfig().m_gyroOffset, true);
    Trace.getInstance().logCommandInfo(this,
        "Setting offset to " + m_configSupplier.getConfig().m_gyroOffset);

  }

  private class SideScoreLeaveHomeConfigSupplier {
    SideScoreLeaveHomeConfig m_config;

    public void setConfig(SideScoreLeaveHomeConfig config) {
      m_config = config;
      System.out.println("SetConfig: Gyro offset " + m_config.m_gyroOffset);
    }

    public SideScoreLeaveHomeConfig getConfig() {
      System.out.println("GetConfig: Gyro offset " + m_config.m_gyroOffset);
      return m_config;
    }
  }

}
