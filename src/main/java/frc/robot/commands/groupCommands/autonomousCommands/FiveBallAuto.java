// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.telemetries.Trace;

public class FiveBallAuto extends SequentialCommandGroup {
  /** Add your docs here. */
  public FiveBallAuto() {
    final double distanceToMove = 65;
    final double maxOutput = 0.5;
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    addCommands(new MoveUsingEncoder(driveTrain, distanceToMove, maxOutput),
        new TurnToCompassHeading(344), new TurnToCompassHeading(100),
        new MoveUsingEncoder(driveTrain, 117.1, maxOutput), new TurnToCompassHeading(80),
        new MoveUsingEncoder(driveTrain, 156, maxOutput));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }
}
