// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.SAMgripperCommands.CloseGripper;
import frc.robot.commands.SAMgripperCommands.OpenGripper;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.MiddleScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.OffFloorPickupPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.TopScorePosition;
import frc.robot.commands.samArmExtendRetractCommands.ExtendRetract;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class NoBumpAutoScoreTwice extends SequentialCommandGroup4905 {
  /** Creates a new AutoPlaceAndLeave. */
  public NoBumpAutoScoreTwice() {
    long waitTime = 250;
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    addCommands(
        new ParallelDeadlineGroup(new SequentialCommandGroup4905(
            new TopScorePosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase(), true, true, false),
            new OpenGripper(subsystemsContainer.getGripper())), new PauseRobot(driveTrain)),

        new PauseRobot(waitTime, driveTrain),

        new ParallelDeadlineGroup(new PauseRobot(500, driveTrain),
            new ExtendRetract(subsystemsContainer.getArmExtRetBase(), 20)),

        new ParallelCommandGroup(
            new SequentialCommandGroup4905(
                new StowPosition(subsystemsContainer.getArmRotateBase(),
                    subsystemsContainer.getArmExtRetBase()),
                new OffFloorPickupPosition(subsystemsContainer.getArmRotateBase(),
                    subsystemsContainer.getArmExtRetBase(), true, true, true)),
            new MoveUsingEncoder(driveTrain, -178, 0.5)),

        new ParallelDeadlineGroup(new CloseGripper(subsystemsContainer.getGripper()),
            new PauseRobot(driveTrain)),

        new PauseRobot(500, driveTrain),

        new ParallelCommandGroup(
            new StowPosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase()),
            new MoveUsingEncoder(driveTrain, 180, 0.85)),

        new ParallelDeadlineGroup(new SequentialCommandGroup(
            new MiddleScorePosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase(), true, true, false),
            new OpenGripper(subsystemsContainer.getGripper())), new PauseRobot(driveTrain)),

        new PauseRobot(waitTime, driveTrain),

        new ParallelDeadlineGroup(
            new ParallelCommandGroup(new StowPosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase()), new PauseRobot(driveTrain))));
  }

  @Override
  public void additionalInitialize() {
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
