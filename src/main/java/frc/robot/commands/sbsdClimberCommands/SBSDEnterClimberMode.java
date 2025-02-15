// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdClimberCommands;

import frc.robot.commands.sbsdArmCommands.MoveArmToClimberMode;
import frc.robot.commands.sbsdArmCommands.MoveEndEffectorToClimberMode;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SBSDEnterClimberMode extends ParallelCommandGroup4905 {
  /** Creates a new SBSDEnterClimberMode. */
  public SBSDEnterClimberMode() {
    // add algae once it is done
    addCommands(new MoveArmToClimberMode(), new MoveEndEffectorToClimberMode());
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {
  }

  // Returns true when the command should end.

}
