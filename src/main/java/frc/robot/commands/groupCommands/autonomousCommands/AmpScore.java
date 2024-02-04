// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.AllianceConfig;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AmpScore extends SequentialCommandGroup4905 {
  DoubleSupplier m_waypoint1;
  DoubleSupplier m_angle1;
  DoubleSupplier m_waypoint2;
  DoubleSupplier m_waypoint3;
  DoubleSupplier m_angle2;
  DoubleSupplier m_waypoint4;
  DoubleSupplier m_waypoint5;
  DoubleSupplier m_angle3;
  DoubleSupplier m_waypoint6;
  DoubleSupplier m_waypoint7;
  DoubleSupplier m_angle4;
  DoubleSupplier m_waypoint8;

  public AmpScore(Config autonomousConfig) {
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
    DriveTrainBase driveTrain = subsystemsContainer.getDriveTrain();

    m_waypoint1 = () -> autonomousConfig.getDouble("AmpScore.WayPoint1");
    m_angle1 = () -> autonomousConfig.getDouble("AmpScore.Angle1");
    m_waypoint2 = () -> autonomousConfig.getDouble("AmpScore.WayPoint2");
    m_waypoint3 = () -> autonomousConfig.getDouble("AmpScore.WayPoint3");
    m_angle2 = () -> autonomousConfig.getDouble("AmpScore.Angle2");
    m_waypoint4 = () -> autonomousConfig.getDouble("AmpScore.WayPoint4");
    m_waypoint5 = () -> autonomousConfig.getDouble("AmpScore.WayPoint5");
    m_angle3 = () -> autonomousConfig.getDouble("AmpScore.Angle3");
    m_waypoint6 = () -> autonomousConfig.getDouble("AmpScore.WayPoint6");
    m_waypoint7 = () -> autonomousConfig.getDouble("AmpScore.WayPoint7");
    m_angle4 = () -> autonomousConfig.getDouble("AmpScore.Angle4");
    m_waypoint8 = () -> autonomousConfig.getDouble("AmpScore.WayPoint8");

    Trace.getInstance().logCommandInfo(this, "waypoint1: " + m_waypoint1.getAsDouble());
    Trace.getInstance().logCommandInfo(this, "waypoint2: " + m_waypoint2.getAsDouble());
    Trace.getInstance().logCommandInfo(this, "angle1: " + m_angle1.getAsDouble());
    Trace.getInstance().logCommandInfo(this, "waypoint3: " + m_waypoint3.getAsDouble());
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

    addCommands(new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, m_waypoint1, 1.0),
        new TurnToCompassHeading(m_angle1), new MoveUsingEncoder(driveTrain, m_waypoint2, 1.0)// ,
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

    ));
  }

  public void additionalInitialize() {
    Config autonomousConfig = AllianceConfig.getCurrentAlliance();

    m_waypoint1 = () -> autonomousConfig.getDouble("AmpScore.WayPoint1");
    m_angle1 = () -> autonomousConfig.getDouble("AmpScore.Angle1");
    m_waypoint2 = () -> autonomousConfig.getDouble("AmpScore.WayPoint2");
    m_waypoint3 = () -> autonomousConfig.getDouble("AmpScore.WayPoint3");
    m_angle2 = () -> autonomousConfig.getDouble("AmpScore.Angle2");
    m_waypoint4 = () -> autonomousConfig.getDouble("AmpScore.WayPoint4");
    m_waypoint5 = () -> autonomousConfig.getDouble("AmpScore.WayPoint5");
    m_angle3 = () -> autonomousConfig.getDouble("AmpScore.Angle3");
    m_waypoint6 = () -> autonomousConfig.getDouble("AmpScore.WayPoint6");
    m_waypoint7 = () -> autonomousConfig.getDouble("AmpScore.WayPoint7");
    m_angle4 = () -> autonomousConfig.getDouble("AmpScore.Angle4");
    m_waypoint8 = () -> autonomousConfig.getDouble("AmpScore.WayPoint8");

  }
}
