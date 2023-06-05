// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import frc.robot.commands.samArmExtendRetractCommands.ExtendRetract;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class BalancingArmPosition extends SequentialCommandGroup4905 {

  private final double m_balancingPosition = 0;

  public BalancingArmPosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(new ExtendRetract(armExtRet));
  }

  @Override
  public void additionalInitialize() {
    ArmRotationExtensionSingleton.getInstance().setPosition(m_balancingPosition);
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
