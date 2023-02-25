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

public class MiddleScorePosition extends SequentialCommandGroup4905 {
  /** Creates a new MiddlePositionScore. */
  private final double m_cubeBackwardMiddleAngle = 112;
  private final double m_cubeBackwardMiddlePosition = 0;
  private final double m_coneBackwardMiddleAngle = 0;
  private final double m_coneBackwardMiddlePosition = 0;
  private final double m_cubeForwardMiddleAngle = 248;
  private final double m_cubeForwardMiddlePosition = 0;
  private final double m_coneForwardMiddleAngle = 0;
  private final double m_coneForwardMiddlePosition = 0;

  public MiddleScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if ((Robot.getInstance().getOIContainer().getSubsystemController().getGrabBackwardButton())
        && (Robot.getInstance().getOIContainer().getSubsystemController().getConeButton())) {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_coneBackwardMiddleAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_coneBackwardMiddlePosition);
    } else if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getGrabBackwardButton()) {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeBackwardMiddleAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeBackwardMiddlePosition);
    } else if (Robot.getInstance().getOIContainer().getSubsystemController().getConeButton()) {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_coneForwardMiddleAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_coneForwardMiddlePosition);
    } else {
      ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeForwardMiddleAngle);
      ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeForwardMiddlePosition);
    }
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
