// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DriveStation2Speaker extends SequentialCommandGroup {
  public DriveStation2Speaker(boolean blueAlliance) {
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
    //
    addCommands(new ParallelDeadlineGroup(
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, -166, 0.5))));
  }
}
