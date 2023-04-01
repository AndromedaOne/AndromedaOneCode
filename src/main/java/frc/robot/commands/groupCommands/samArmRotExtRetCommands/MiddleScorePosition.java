// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import frc.robot.Robot;
import frc.robot.commands.samArmExtendRetractCommands.ExtendRetract;
import frc.robot.commands.samArmRotateCommands.RotateArm;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class MiddleScorePosition extends SequentialCommandGroup4905 {
  /** Creates a new MiddlePositionScore. */
  private final double m_cubeBackwardMiddleAngle = 240;
  private final double m_cubeBackwardMiddlePosition = 10;
  private final double m_coneBackwardMiddleAngle = 231;
  private final double m_coneBackwardMiddlePosition = 12;
  private final double m_cubeForwardMiddleAngle = 120;
  private final double m_cubeForwardMiddlePosition = 12;
  private final double m_coneForwardMiddleAngle = 128;
  private final double m_coneForwardMiddlePosition = 21;
  private boolean m_auto = false;
  private boolean m_cube = false;
  private boolean m_backwards = false;

  public MiddleScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet, boolean auto,
      boolean cube, boolean backwards) {
    m_auto = auto;
    m_cube = cube;
    m_backwards = backwards;
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true),
        new ExtendRetract(armExtRet));
  }

  public MiddleScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    // cube and backwards booleans are ignored when not in auto
    this(armRotate, armExtRet, false, true, true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (m_auto) {
      if (m_cube) {
        if (m_backwards) {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeBackwardMiddleAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeBackwardMiddlePosition);
        } else {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeForwardMiddleAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeForwardMiddlePosition);
        }
      } else {
        if (m_backwards) {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_coneBackwardMiddleAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_coneBackwardMiddlePosition);
        } else {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_coneForwardMiddleAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_coneForwardMiddlePosition);
        }
      }
    } else {
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
    }

    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
