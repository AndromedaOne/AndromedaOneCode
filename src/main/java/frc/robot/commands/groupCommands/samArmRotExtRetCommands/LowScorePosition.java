// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import frc.robot.Robot;
import frc.robot.commands.samArmRotateCommands.RotateArm;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class LowScorePosition extends SequentialCommandGroup4905 {
  /** Creates a new LowPositionScore. */
  private final double m_backwardLowAngle = 71;
  private final double m_backwardLowPosition = 0;
  private final double m_forwardLowAngle = 289;
  private final double m_forwardLowPosition = 0;

  public LowScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (Robot.getInstance().getOIContainer().getSubsystemController().getGrabBackwardButton()) {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_forwardLowAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_forwardLowPosition);
    } else {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_backwardLowAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_backwardLowPosition);
    }
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
