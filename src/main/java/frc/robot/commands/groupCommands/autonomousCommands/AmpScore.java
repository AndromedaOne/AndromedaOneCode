// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AmpScore extends SequentialCommandGroup {
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
    double waypoint1 = autonomousConfig.getDouble("AmpScore.Waypoint1");
    double angle1 = autonomousConfig.getDouble("AmpScore.Angle1");
    double waypoint2 = autonomousConfig.getDouble("AmpScore.Waypoint2");
    double waypoint3 = autonomousConfig.getDouble("AmpScore.Waypoint3");
    double angle2 = autonomousConfig.getDouble("AmpScore.Angle2");
    double waypoint4 = autonomousConfig.getDouble("AmpScore.Waypoint4");
    double waypoint5 = autonomousConfig.getDouble("AmpScore.Waypoint5");
    double angle3 = autonomousConfig.getDouble("AmpScore.Angle3");
    double waypoint6 = autonomousConfig.getDouble("AmpScore.Waypoint6");
    double waypoint7 = autonomousConfig.getDouble("AmpScore.Waypoint7");
    double angle4 = autonomousConfig.getDouble("AmpScore.Angle4");
    double waypoint8 = autonomousConfig.getDouble("AmpScore.Waypoint8");
    //
    addCommands(new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint1, 1.0)),
        new SequentialCommandGroup4905(new TurnToCompassHeading(angle1)),
        new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint2, 1.0)),
        // need amp score command
        new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint3, 1.0)),
        new SequentialCommandGroup4905(new TurnToCompassHeading(angle2)),
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, waypoint4, 1.0)// , need intake
                                                                                 // command
        ), new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint5, 1.0)),
        new SequentialCommandGroup4905(new TurnToCompassHeading(angle3)),
        new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint6, 1.0)),
        // need amp score command
        new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint7, 1.0)),
        new SequentialCommandGroup4905(new TurnToCompassHeading(angle4)),
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, waypoint8, 1.0)// , need intake
                                                                                 // command
        )

    );
  }
}
