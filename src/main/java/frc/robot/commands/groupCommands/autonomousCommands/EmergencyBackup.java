// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import com.typesafe.config.Config;

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
public class EmergencyBackup extends SequentialCommandGroup {
  public EmergencyBackup(Config autonomousConfig) {
    // Both
    // Positioned by the Amp
    // Move foward out of the starting zone and park
    // wait for teleop
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrainBase driveTrain = subsystemsContainer.getDriveTrain();
    double waypoint = autonomousConfig.getDouble("EmergencyBackup.Waypoint1");
    //
    addCommands(new ParallelDeadlineGroup(
        new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, waypoint, 1.0))));
  }
}
