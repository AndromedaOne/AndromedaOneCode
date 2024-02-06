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
  }

  AmpScoreConfig ampScoreConfigRed = new AmpScoreConfig();
  AmpScoreConfig ampScoreConfigBlue = new AmpScoreConfig();
  DriveTrainBase m_driveTrain;

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
    Config redConfig = Config4905.getConfig4905().getRedAutonomousConfig();
    ampScoreConfigRed.m_waypoint1 = redConfig.getDouble("AmpScore.WayPoint1");
    ampScoreConfigRed.m_angle1 = redConfig.getDouble("AmpScore.Angle1");
    ampScoreConfigRed.m_waypoint2 = redConfig.getDouble("AmpScore.WayPoint2");
    ampScoreConfigRed.m_waypoint3 = redConfig.getDouble("AmpScore.WayPoint3");
    ampScoreConfigRed.m_angle2 = redConfig.getDouble("AmpScore.Angle2");
    ampScoreConfigRed.m_waypoint4 = redConfig.getDouble("AmpScore.WayPoint4");
    ampScoreConfigRed.m_waypoint5 = redConfig.getDouble("AmpScore.WayPoint5");
    ampScoreConfigRed.m_angle3 = redConfig.getDouble("AmpScore.Angle3");
    ampScoreConfigRed.m_waypoint6 = redConfig.getDouble("AmpScore.WayPoint6");
    ampScoreConfigRed.m_waypoint7 = redConfig.getDouble("AmpScore.WayPoint7");
    ampScoreConfigRed.m_angle4 = redConfig.getDouble("AmpScore.Angle4");
    ampScoreConfigRed.m_waypoint8 = redConfig.getDouble("AmpScore.WayPoint8");

    Config blueConfig = Config4905.getConfig4905().getBlueAutonomousConfig();
    ampScoreConfigBlue.m_waypoint1 = blueConfig.getDouble("AmpScore.WayPoint1");
    ampScoreConfigBlue.m_angle1 = blueConfig.getDouble("AmpScore.Angle1");
    ampScoreConfigBlue.m_waypoint2 = blueConfig.getDouble("AmpScore.WayPoint2");
    ampScoreConfigBlue.m_waypoint3 = blueConfig.getDouble("AmpScore.WayPoint3");
    ampScoreConfigBlue.m_angle2 = blueConfig.getDouble("AmpScore.Angle2");
    ampScoreConfigBlue.m_waypoint4 = blueConfig.getDouble("AmpScore.WayPoint4");
    ampScoreConfigBlue.m_waypoint5 = blueConfig.getDouble("AmpScore.WayPoint5");
    ampScoreConfigBlue.m_angle3 = blueConfig.getDouble("AmpScore.Angle3");
    ampScoreConfigBlue.m_waypoint6 = blueConfig.getDouble("AmpScore.WayPoint6");
    ampScoreConfigBlue.m_waypoint7 = blueConfig.getDouble("AmpScore.WayPoint7");
    ampScoreConfigBlue.m_angle4 = blueConfig.getDouble("AmpScore.Angle4");
    ampScoreConfigBlue.m_waypoint8 = blueConfig.getDouble("AmpScore.WayPoint8");

    //
    // addCommands(new SequentialCommandGroup(new MoveUsingEncoder(driveTrain,
    // waypoint1, 1.0)),
    // new SequentialCommandGroup(new TurnToCompassHeading(angle1)),
    // new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, waypoint2, 1.0)),
    // need amp score command
    // new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, waypoint3, 1.0)),
    // new SequentialCommandGroup(new TurnToCompassHeading(angle2)),
    // new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, waypoint4, 1.0)// ,
    // need intake
    // command
    // ), new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, waypoint5,
    // 1.0)),
    // new SequentialCommandGroup(new TurnToCompassHeading(angle3)),
    // new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, waypoint6, 1.0)),
    // need amp score command
    // new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, waypoint7, 1.0)),
    // new SequentialCommandGroup(new TurnToCompassHeading(angle4)),
    // new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, waypoint8, 1.0)// ,
    // need intake
    // command
    // )

    // );

    // need amp score command
    // new MoveUsingEncoder(driveTrain, waypoint3, 1.0), new
    // TurnToCompassHeading(angle2),
    // new ParallelCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint4,
    // 1.0)// ,
    // need intake command
    // ), new MoveUsingEncoder(driveTrain, waypoint5, 1.0), new
    // TurnToCompassHeading(angle3),
    // new MoveUsingEncoder(driveTrain, waypoint6, 1.0),
    // need amp score command
    // new MoveUsingEncoder(driveTrain, waypoint7, 1.0), new
    // TurnToCompassHeading(angle4),
    // new ParallelCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint8,
    // 1.0)// ,
    // need intake

    // ));
  }

  public void additionalInitialize() {
    AmpScoreConfig config;
    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance == Alliance.Blue) {
      config = ampScoreConfigBlue;
    } else {
      config = ampScoreConfigRed;
    }
    CommandScheduler.getInstance()
        .schedule(new SequentialCommandGroup4905(
            new MoveUsingEncoder(m_driveTrain, config.m_waypoint1, 0.5),
            new TurnToCompassHeading(config.m_angle1), new PauseRobot(40, m_driveTrain),
            new MoveUsingEncoder(m_driveTrain, config.m_waypoint2, 0.5),
            // need amp score command
            new MoveUsingEncoder(m_driveTrain, config.m_waypoint3, 0.5),
            new TurnToCompassHeading(config.m_angle2), new PauseRobot(40, m_driveTrain),
            new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint4, 0.5)// ,
            // need intake command
            ), new MoveUsingEncoder(m_driveTrain, config.m_waypoint5, 0.5),
            new TurnToCompassHeading(config.m_angle3), new PauseRobot(40, m_driveTrain),
            new MoveUsingEncoder(m_driveTrain, config.m_waypoint6, 0.5),
            // need amp score command
            new MoveUsingEncoder(m_driveTrain, config.m_waypoint7, 0.5),
            new TurnToCompassHeading(config.m_angle4), new PauseRobot(40, m_driveTrain),
            new ParallelCommandGroup4905(new MoveUsingEncoder(m_driveTrain, config.m_waypoint8, 0.5)// ,
            // need intake

            )));
  }
}
