// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.samArmExtendRetractCommands.ExtendRetract;
import frc.robot.commands.samArmRotateCommands.EnableArmBrake;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DefaultArmRotExt extends ParallelCommandGroup {
  /** Creates a new DefaultArmRotExt. */
  public DefaultArmRotExt(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {

    addCommands(new EnableArmBrake(armRotate), new ExtendRetract(armExtRet,
        ArmRotationExtensionSingleton.getInstance().getPosition(), false));
  }
}
