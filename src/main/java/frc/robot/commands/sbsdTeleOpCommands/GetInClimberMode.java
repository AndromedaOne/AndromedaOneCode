// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdTeleOpCommands;

import frc.robot.commands.sbsdArmCommands.MoveArmToClimberMode;
import frc.robot.commands.sbsdArmCommands.MoveEndEffectorToClimberMode;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.sbsdclimber.ClimberMode;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class GetInClimberMode extends SequentialCommandGroup4905 {

  public GetInClimberMode() {
    addCommands(new ParallelCommandGroup4905(new MoveArmToClimberMode(),
        new MoveEndEffectorToClimberMode()));
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    Trace.getInstance().logCommandInfo(this,
        "In Climber Mode: " + ClimberMode.getInstance().getInClimberMode());
  }
}
