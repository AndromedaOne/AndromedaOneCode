// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.BalanceRobot;
import frc.robot.commands.driveTrainCommands.EnableParkingBrake;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.MoveWithoutPID;
import frc.robot.commands.groupCommands.samArmRotExtRetCommands.BalancingArmPosition;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CSAutoLeaveEngage extends SequentialCommandGroup4905 {
  /** Creates a new EngageAutoDock. */
  public CSAutoLeaveEngage() {
    final double distanceToMove = -156;
    final double maxOutPut = 0.5;
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    MoveUsingEncoder moveCommand = new MoveUsingEncoder(driveTrain, distanceToMove, maxOutPut);
    addCommands(moveCommand,
        new BalancingArmPosition(subsystemsContainer.getArmRotateBase(),
            subsystemsContainer.getArmExtRetBase()),
        new MoveWithoutPID(driveTrain, 55, 0.75, 180), new BalanceRobot(driveTrain, 0.5, 180),
        new EnableParkingBrake(driveTrain));

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