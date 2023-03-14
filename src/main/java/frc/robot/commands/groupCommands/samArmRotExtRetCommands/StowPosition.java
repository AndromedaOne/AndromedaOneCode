// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import frc.robot.commands.samArmExtendRetractCommands.ExtendRetractInternal;
import frc.robot.commands.samArmRotateCommands.RotateArm;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class StowPosition extends SequentialCommandGroup4905 {
  /** Creates a new StowPosition. */
  private final double m_stowAngle = 180;
  private final double m_stowPosition = 0;

  public StowPosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
    new ExtendRetractInternal(armExtRet, ArmRotationExtensionSingleton.getInstance().getPosition(),
        true);
  }

  @Override
  public void additionalInitialize() {
    ArmRotationExtensionSingleton.getInstance().setAngle(m_stowAngle);
    ArmRotationExtensionSingleton.getInstance().setPosition(m_stowPosition);
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
