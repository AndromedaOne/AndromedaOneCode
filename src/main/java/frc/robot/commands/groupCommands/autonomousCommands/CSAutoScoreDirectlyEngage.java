// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.Robot;
import frc.robot.commands.SAMgripperCommands.OpenGripper;
import frc.robot.commands.driveTrainCommands.BalanceRobot;
import frc.robot.commands.driveTrainCommands.EnableParkingBrake;
import frc.robot.commands.driveTrainCommands.MoveWithoutPID;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.BalancingArmPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.TopScorePosition;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CSAutoScoreDirectlyEngage extends SequentialCommandGroup4905 {
  /** Creates a new PlaceDirectlyEngage. */
  public CSAutoScoreDirectlyEngage() {
    long waitTime = 250;
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    addCommands(
        new ParallelDeadlineGroup(new SequentialCommandGroup4905(
            new TopScorePosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase(), true, true, false),
            new OpenGripper(subsystemsContainer.getGripper())), new PauseRobot(driveTrain)),

        new PauseRobot(waitTime, driveTrain),

        new SequentialCommandGroup4905(
            new StowPosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase()),
            new BalancingArmPosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase()),
            new MoveWithoutPID(driveTrain, -90, 0.75, 180), new BalanceRobot(driveTrain, 0.6, 180),
            new EnableParkingBrake(driveTrain)));
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
