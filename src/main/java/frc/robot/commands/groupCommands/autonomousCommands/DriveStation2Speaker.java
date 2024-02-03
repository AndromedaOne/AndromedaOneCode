// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DriveStation2Speaker extends SequentialCommandGroup4905 {
  public DriveStation2Speaker(Config autonomousConfig) {
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
    DriveTrainBase driveTrain = subsystemsContainer.getDriveTrain();
    double waypoint1 = autonomousConfig.getDouble("CentralSpeaker2Scores.WayPoint1");
    double angle1 = autonomousConfig.getDouble("CentralSpeaker2Scores.Angle1");
    double waypoint2 = autonomousConfig.getDouble("CentralSpeaker2Scores.WayPoint2");
    double angle2 = autonomousConfig.getDouble("CentralSpeaker2Scores.Angle2");
    double waypoint3 = autonomousConfig.getDouble("CentralSpeaker2Scores.WayPoint3");
    //
    addCommands(new SequentialCommandGroup4905(
        // need shoot command
        new MoveUsingEncoder(driveTrain, waypoint1, 1.0), new TurnToCompassHeading(angle1),
        new ParallelCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint2, 1.0)// ,need intake
        // command
        ), new TurnToCompassHeading(angle2),
        // need shoot command
        new MoveUsingEncoder(driveTrain, waypoint3, 1.0)

    ));
  }
}
