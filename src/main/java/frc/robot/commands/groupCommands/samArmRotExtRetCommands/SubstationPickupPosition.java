// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import frc.robot.Robot;
import frc.robot.commands.samArmExtendRetractCommands.ExtendRetractInternal;
import frc.robot.commands.samArmRotateCommands.RotateArm;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class SubstationPickupPosition extends SequentialCommandGroup4905 {
  /** Creates a new SubstationPickupPosition. */
  private final double m_substationAngle = 113;
  private final double m_substationPosition = 33;
  private final double m_backwardSubstationAngle = 242;
  private final double m_backwardSubstationPosition = 36;

  public SubstationPickupPosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
    new ExtendRetractInternal(armExtRet, ArmRotationExtensionSingleton.getInstance().getPosition(),
        true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (Robot.getInstance().getOIContainer().getSubsystemController().getGrabBackwardButton()) {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_backwardSubstationAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_backwardSubstationPosition);
    } else {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_substationAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_substationPosition);
    }
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
