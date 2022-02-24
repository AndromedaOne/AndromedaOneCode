// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.groupCommands.DelayedSequentialCommandGroup;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class TaxiAuto extends CommandBase {
  /** Add your docs here. */
  public TaxiAuto() {
    final double distanceToMove = 48;
    final double maxOutput = 0.6;
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, distanceToMove, maxOutput));
  }
}
