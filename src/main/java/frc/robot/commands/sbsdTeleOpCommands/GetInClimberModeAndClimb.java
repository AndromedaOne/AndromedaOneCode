// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import frc.robot.commands.sbsdArmCommands.MoveArmToClimberMode;
import frc.robot.commands.sbsdArmCommands.MoveEndEffectorToClimberMode;
import frc.robot.commands.sbsdClimberCommands.SBSDClimb;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class GetInClimberModeAndClimb extends SequentialCommandGroup4905 {

  public GetInClimberModeAndClimb() {
    addCommands(new ParallelCommandGroup4905(new MoveArmToClimberMode(),
        new MoveEndEffectorToClimberMode()), new SBSDClimb());
  }
}
