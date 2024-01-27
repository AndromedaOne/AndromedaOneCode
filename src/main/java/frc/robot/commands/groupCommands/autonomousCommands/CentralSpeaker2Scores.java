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
public class CentralSpeaker2Scores extends SequentialCommandGroup {
  public CentralSpeaker2Scores(boolean blueAlliance) {
    // List of what this auto mode should do:
    // Both
    // 1. Positioned at Central Speaker.
    // 2. Shoot and score the preloaded note into the speaker.
    // 3. drive forward.
    // Red instructions
    // 4. drive forward and pick up the R2 Note
    // Blue instructions
    // 4. drive forward and pick up the B2 Note
    // 5. Drive Backwards if necessary to shoot the note into the speaker.
    // 6. Shoot and score the note into the speaker.
    // Red instructions
    // 7. Drive forward and then diagonally right and then diagonally left to
    // go around stage to get to C2 Note
    // Blue instructions
    // 7. Drive forward and then diagonally left and then diagonally right to
    // go around stage to get to C2 Note
    // Both
    // 8. Pick up note C2
    // 9. Start driving backward towards speaker so the robot can score the note in
    // Teleop.
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrainBase driveTrain = subsystemsContainer.getDriveTrain();
    //
    addCommands(new ParallelDeadlineGroup(
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, -166, 0.5))));
  }
}