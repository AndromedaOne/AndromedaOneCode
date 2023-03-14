// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import frc.robot.commands.groupCommands.samArmRotExtRetCommands.ArmRotationExtensionSingleton;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;

public class ExtendRetract extends SequentialCommandGroup4905 {
  /** Creates a new ExtRetSeq. */
  public ExtendRetract(SamArmExtRetBase armExtRet) {
    addCommands(new InitializeArmExtRet(armExtRet), new ExtendRetractInternal(armExtRet,
        ArmRotationExtensionSingleton.getInstance().getPosition(), true));
  }

}
