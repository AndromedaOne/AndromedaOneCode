// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpointsSupplier;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class EndEffectorControlCommand extends SequentialCommandGroup4905 {
  /** Creates a new EndEffectorControlCommand. */
  public EndEffectorControlCommand(ArmSetpointsSupplier level, boolean doesEnd) {
    addCommands(new InitializeEERotate(), new EndEffectorControlCommandInternal(level, doesEnd));
  }

  public EndEffectorControlCommand(boolean useSmartDashboard, boolean doesEnd) {
    addCommands(new InitializeEERotate(),
        new EndEffectorControlCommandInternal(useSmartDashboard, doesEnd));
  }
}
