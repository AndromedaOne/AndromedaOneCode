// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.Robot;
import frc.robot.commands.SAMgripperCommands.OpenGripper;
import frc.robot.commands.driveTrainCommands.BalanceRobot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.MoveWithoutPID;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.MiddleScorePosition;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.StowPosition;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SafetyAutoCS extends SequentialCommandGroup4905 {
  /** Creates a new SafetyAutoCS. */
  public SafetyAutoCS() {
    final double distanceToMove = 146;
    final double maxOutPut = 0.5;
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    Gyro4905 m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    // This assumes robot faces south. We will place cube extending backwards, drive
    // over the
    // charging station leaving the community zone, and drive back onto the station
    // to engage.
    MoveUsingEncoder moveCommand = new MoveUsingEncoder(driveTrain, distanceToMove, maxOutPut);
    m_gyro.setInitialZangleOffset(0);
    addCommands(
        new ParallelDeadlineGroup(new SequentialCommandGroup4905(
            new MiddleScorePosition(subsystemsContainer.getArmRotateBase(),
                subsystemsContainer.getArmExtRetBase(), true, true, true),
            new OpenGripper(subsystemsContainer.getGripper())), new PauseRobot(driveTrain)),
        new PauseRobot(250, driveTrain),
        new ParallelCommandGroup(new StowPosition(subsystemsContainer.getArmRotateBase(),
            subsystemsContainer.getArmExtRetBase()), moveCommand),

        new SequentialCommandGroup4905(new MoveWithoutPID(driveTrain, -45, 0.75, 0),
            new BalanceRobot(driveTrain, 0.5, 0)));
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