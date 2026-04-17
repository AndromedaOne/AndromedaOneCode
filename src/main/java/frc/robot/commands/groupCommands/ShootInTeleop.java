// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import frc.robot.commands.FuelRaiderCommands.WiggleAHI;
import frc.robot.commands.Timer;
import frc.robot.commands.ejectBeltCommands.EjectBeltShoot;
import frc.robot.commands.intakeCommands.IntakeRollerIntakeCommand;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShootInTeleop extends ParallelCommandGroup4905 {
  /** Creates a new ShootInTeleop. */
  public ShootInTeleop() {
    addCommands(new SequentialCommandGroup4905(new Timer((long) 1000.0), new WiggleAHI()),
        new EjectBeltShoot(), new IntakeRollerIntakeCommand());
  }
}
