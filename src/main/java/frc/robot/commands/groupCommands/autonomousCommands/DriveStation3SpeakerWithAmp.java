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
public class DriveStation3SpeakerWithAmp extends SequentialCommandGroup4905 {
  public DriveStation3SpeakerWithAmp(Config autonomousConfig) {
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
    DriveTrainBase driveTrain = subsystemsContainer.getDriveTrain();
    double waypoint1 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.WayPoint1");
    double angle1 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.Angle1");
    double waypoint2 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.WayPoint2");
    double waypoint3 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.WayPoint3");
    double angle2 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.Angle2");
    double waypoint4 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.WayPoint4");
    double waypoint5 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.WayPoint5");
    double angle3 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.Angle3");
    double waypoint6 = autonomousConfig.getDouble("DriveStation3SpeakerWithAmp.WayPoint6");
    //
    addCommands(new SequentialCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint1, 1.0),
        new TurnToCompassHeading(angle1),
        new ParallelCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint2, 1.0)// Need Intake
        // Command
        ), new MoveUsingEncoder(driveTrain, waypoint3, 1.0), new TurnToCompassHeading(angle2),
        new MoveUsingEncoder(driveTrain, waypoint4, 1.0),
        // add shoot command
        new MoveUsingEncoder(driveTrain, waypoint5, 1.0), new TurnToCompassHeading(angle3),
        new ParallelCommandGroup4905(new MoveUsingEncoder(driveTrain, waypoint6, 1.0)// ,Need Intake
        // Command
        )));
  }
}
