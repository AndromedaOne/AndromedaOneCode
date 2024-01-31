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
public class CentralSpeaker3Scores extends SequentialCommandGroup {
  public CentralSpeaker3Scores(Config autonomousConfig) {
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
    DriveTrainBase driveTrain = subsystemsContainer.getDriveTrain();
    double waypoint1 = autonomousConfig.getDouble("CentralSpeaker3Scores.WayPoint1");
    double waypoint2 = autonomousConfig.getDouble("CentralSpeaker3Scores.WayPoint2");
    double angle1 = autonomousConfig.getDouble("CentralSpeaker3Scores.Angle1");
    double waypoint3 = autonomousConfig.getDouble("CentralSpeaker3Scores.WayPoint3");
    double angle2 = autonomousConfig.getDouble("CentralSpeaker3Scores.Angle2");
    double waypoint4 = autonomousConfig.getDouble("CentralSpeaker3Scores.WayPoint4");
    double angle3 = autonomousConfig.getDouble("CentralSpeaker3Scores.Angle3");
    //
    addCommands(
        // need shooter command
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, waypoint1, 1.0)// , need intake
                                                                                 // command
        ),
        // need shooter command
        new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint2, 1.0)),
        new SequentialCommandGroup4905(new TurnToCompassHeading(angle1)),
        new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint3, 1.0)),
        new SequentialCommandGroup4905(new TurnToCompassHeading(angle2)),
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, waypoint4, 1.0)// , need intake
                                                                                 // command
        ), new SequentialCommandGroup4905(new TurnToCompassHeading(angle3))
    // need shooter command

    );
  }
}
