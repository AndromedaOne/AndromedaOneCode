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

public class TopScorePosition extends SequentialCommandGroup4905 {
  /** Creates a new TopPositionScore. */
  private final double m_backwardTopAngle = 0;
  private final double m_backwardTopPosition = 0;
  private final double m_forwardTopAngle = 0;
  private final double m_forwardTopPosition = 0;

  public TopScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (Robot.getInstance().getOIContainer().getSubsystemController().getGrabBackwardButton()) {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_forwardTopAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_forwardTopPosition);
    } else {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_backwardTopAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_backwardTopPosition);
    }
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
