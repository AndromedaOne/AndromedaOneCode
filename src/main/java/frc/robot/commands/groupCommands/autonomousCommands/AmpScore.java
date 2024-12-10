// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotateCommand;
import frc.robot.commands.billthovenEndEffectorPositionCommands.MoveEndEffector;
import frc.robot.commands.billthovenFeederCommands.FeederStates;
import frc.robot.commands.billthovenFeederCommands.RunBillFeeder;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.groupCommands.DelayedSequentialCommandGroup;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.DrivePositionCommand;
import frc.robot.commands.groupCommands.billthovenShooterIntakeCommands.IntakeNote;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
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
public class AmpScore extends SequentialCommandGroup4905 {
  private class AmpScoreConfig {
    double m_waypoint1;
    double m_angle1;
    double m_waypoint2;
    double m_waypoint3;
    double m_angle2;
    double m_waypoint4;
    double m_waypoint5;
    double m_angle3;
    double m_waypoint6;
    double m_waypoint7;
    double m_angle4;
    double m_waypoint8;

    public String toString() {
      String str = new String("\twaypt 1: " + m_waypoint1 + "\n\tang 1: " + m_angle1
          + "\n\twaypt 2:" + m_waypoint2 + "\n\tandg 2: " + m_angle2 + "\n\twaypt 3: " + m_waypoint3
          + "\n\tang 3: " + m_angle3 + "\n\twaypt 4: " + m_waypoint4 + "\n\twaypt 5: " + m_waypoint5
          + "\n\tang 3: " + m_angle3 + "\n\twaypt 6: " + m_waypoint6 + "\n\twaypt 7: " + m_waypoint7
          + "\n\tang 4: " + m_angle4 + "\n\twaypt 8: " + m_waypoint8 + "\n");
      return str;
    }
  }

  private AmpScoreConfig m_ampScoreConfigRed = new AmpScoreConfig();
  private AmpScoreConfig m_ampScoreConfigBlue = new AmpScoreConfig();
  private DriveTrainBase m_driveTrain;
  private BillEndEffectorPositionBase m_endEffector;
  private ArmTestBenchRotateBase m_armRotate;
  private BillFeederBase m_feeder;
  private BillShooterBase m_shooter = Robot.getInstance().getSubsystemsContainer().getBillShooter();
  private AmpScoreConfigSupplier m_ampScoreConfigSupplier = new AmpScoreConfigSupplier();

  public AmpScore() {
    // Robot is started straight
    // for blue move left to the Amp
    // (for red move right)
    // Score preloaded Note in the amp with Head(endifector)
    // for blue strafe foward and Right to Note B1 and rotate 90 degrees to
    // left from front
    // (for Red strafe foward and left to Note R1 and rotate 90 degrees to
    // right from front)
    // pick up Note R1/B1
    // For blue strafe back and to the left then turn 90 degrees to the right from
    // front
    // (for Red strafe back and to the right then turn 90 degrees to the left from
    // front)
    // score picked up Note in Amp
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    m_driveTrain = subsystemsContainer.getDriveTrain();
    m_endEffector = subsystemsContainer.getBillEffectorPosition();
    m_armRotate = subsystemsContainer.getBillArmRotate();
    m_feeder = subsystemsContainer.getBillFeeder();
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    m_ampScoreConfigRed.m_waypoint1 = redConfig.getDouble("AmpScore.WayPoint1");
    m_ampScoreConfigRed.m_angle1 = redConfig.getDouble("AmpScore.Angle1");
    m_ampScoreConfigRed.m_waypoint2 = redConfig.getDouble("AmpScore.WayPoint2");
    m_ampScoreConfigRed.m_waypoint3 = redConfig.getDouble("AmpScore.WayPoint3");
    m_ampScoreConfigRed.m_angle2 = redConfig.getDouble("AmpScore.Angle2");
    m_ampScoreConfigRed.m_waypoint4 = redConfig.getDouble("AmpScore.WayPoint4");
    m_ampScoreConfigRed.m_waypoint5 = redConfig.getDouble("AmpScore.WayPoint5");
    m_ampScoreConfigRed.m_angle3 = redConfig.getDouble("AmpScore.Angle3");
    m_ampScoreConfigRed.m_waypoint6 = redConfig.getDouble("AmpScore.WayPoint6");
    m_ampScoreConfigRed.m_waypoint7 = redConfig.getDouble("AmpScore.WayPoint7");
    m_ampScoreConfigRed.m_angle4 = redConfig.getDouble("AmpScore.Angle4");
    m_ampScoreConfigRed.m_waypoint8 = redConfig.getDouble("AmpScore.WayPoint8");

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    m_ampScoreConfigBlue.m_waypoint1 = blueConfig.getDouble("AmpScore.WayPoint1");
    m_ampScoreConfigBlue.m_angle1 = blueConfig.getDouble("AmpScore.Angle1");
    m_ampScoreConfigBlue.m_waypoint2 = blueConfig.getDouble("AmpScore.WayPoint2");
    m_ampScoreConfigBlue.m_waypoint3 = blueConfig.getDouble("AmpScore.WayPoint3");
    m_ampScoreConfigBlue.m_angle2 = blueConfig.getDouble("AmpScore.Angle2");
    m_ampScoreConfigBlue.m_waypoint4 = blueConfig.getDouble("AmpScore.WayPoint4");
    m_ampScoreConfigBlue.m_waypoint5 = blueConfig.getDouble("AmpScore.WayPoint5");
    m_ampScoreConfigBlue.m_angle3 = blueConfig.getDouble("AmpScore.Angle3");
    m_ampScoreConfigBlue.m_waypoint6 = blueConfig.getDouble("AmpScore.WayPoint6");
    m_ampScoreConfigBlue.m_waypoint7 = blueConfig.getDouble("AmpScore.WayPoint7");
    m_ampScoreConfigBlue.m_angle4 = blueConfig.getDouble("AmpScore.Angle4");
    m_ampScoreConfigBlue.m_waypoint8 = blueConfig.getDouble("AmpScore.WayPoint8");
    m_ampScoreConfigSupplier.setAmpScoreConfig(m_ampScoreConfigRed);
    addCommands(new DelayedSequentialCommandGroup(
        new ParallelCommandGroup4905(new ArmRotateCommand(m_armRotate, () -> 300, true),
            new MoveEndEffector(m_endEffector, () -> true),
            new MoveUsingEncoder(m_driveTrain,
                () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint1, 1)),

        new TurnToCompassHeading(() -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_angle1),
        new PauseRobot(40, m_driveTrain),

        new MoveUsingEncoder(m_driveTrain,
            () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint2, 1),
        new RunBillFeeder(m_feeder, FeederStates.AMPSHOOTING),
        new MoveUsingEncoder(m_driveTrain,
            () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint3, 1),
        new TurnToCompassHeading(() -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_angle2),
        new ParallelCommandGroup4905(
            new ArmRotateCommand(m_armRotate, () -> 350, true, false, true, false),
            new MoveEndEffector(m_endEffector, () -> false)),

        new PauseRobot(40, m_driveTrain),
        new ParallelCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain,
                () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint4, 0.25),
            new IntakeNote(m_armRotate, m_endEffector, m_feeder, m_shooter, false)),
        new ParallelCommandGroup4905(new ArmRotateCommand(m_armRotate, () -> 300, true),
            new MoveEndEffector(m_endEffector, () -> true),
            new MoveUsingEncoder(m_driveTrain,
                () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint5, 1)),

        new TurnToCompassHeading(() -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_angle3),
        new PauseRobot(40, m_driveTrain),

        new MoveUsingEncoder(m_driveTrain,
            () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint6, 1),
        new RunBillFeeder(m_feeder, FeederStates.AMPSHOOTING),
        new MoveUsingEncoder(m_driveTrain,
            () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint7, 1),
        new TurnToCompassHeading(() -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_angle4),
        new PauseRobot(40, m_driveTrain), new DrivePositionCommand(m_endEffector, m_armRotate),
        new MoveUsingEncoder(m_driveTrain,
            () -> m_ampScoreConfigSupplier.getAmpScoreConfig().m_waypoint8, 1)));

  }

  public void additionalInitialize() {
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      m_ampScoreConfigSupplier.setAmpScoreConfig(m_ampScoreConfigBlue);
    } else {
      m_ampScoreConfigSupplier.setAmpScoreConfig(m_ampScoreConfigRed);
    }
    Trace.getInstance().logCommandInfo(this,
        m_ampScoreConfigSupplier.getAmpScoreConfig().toString());
  }

  private class AmpScoreConfigSupplier {
    AmpScoreConfig m_ampScoreConfig;

    public void setAmpScoreConfig(AmpScoreConfig config) {
      m_ampScoreConfig = config;
    }

    public AmpScoreConfig getAmpScoreConfig() {
      return (m_ampScoreConfig);
    }
  }
}
