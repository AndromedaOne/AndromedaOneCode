// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import frc.robot.commands.FuelRaiderCommands.WiggleAHI;
import frc.robot.commands.Timer;
import frc.robot.commands.driveTrainCommands.SwerveDriveSetVelocityToZero;
import frc.robot.commands.intakeCommands.IntakeRollerIntakeCommand;
import frc.robot.rewrittenWPIclasses.ParallelDeadlineGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShootInAuto extends SequentialCommandGroup4905 {
  /** Creates a new Shoot. */

  public ShootInAuto(long timeInMs) {
    // Use addRequirements() here to declare subsystem dependencies.
    addCommands(new SwerveDriveSetVelocityToZero(),
        new ParallelDeadlineGroup4905(new Timer(timeInMs), new ShootBasedOnDistance(),
            new TurnToHubAndRunEjectBelts(), new WiggleAHI(), new IntakeRollerIntakeCommand()));
  }
}
