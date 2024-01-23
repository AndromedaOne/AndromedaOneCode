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
public class AmpScore extends SequentialCommandGroup {
  public AmpScore(boolean blueAlliance) {
    // 1 Robot is started straight
    // 2 for blue move left to the Amp
    // (for red move right)
    // 3 Score preloaded Note in the amp with Head(endifector)
    // 4 for blue strafe foward and Right to nearest Note and rotate 90 degrees to
    // left from front
    // (for Red strafe foward and left to Nearest Note and rotate 90 degrees to
    // right from front)
    // 5 pick up Note
    // 6 For blue strafe back and to the left then turn 90 degrees to the right from
    // front
    // (for Red strafe back and to the right then turn 90 degrees to the left from
    // front)
    // 7 score picked up Note in Amp
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrainBase driveTrain = subsystemsContainer.getDriveTrain();
    //
    addCommands(new ParallelDeadlineGroup(
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, -166, 0.5))));
  }
}
