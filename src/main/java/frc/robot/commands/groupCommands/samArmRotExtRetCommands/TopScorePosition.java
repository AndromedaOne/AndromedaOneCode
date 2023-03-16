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

public class TopScorePosition extends SequentialCommandGroup4905 {
  /** Creates a new TopPositionScore. */
  private final double m_cubeBackwardTopAngle = 236;
  private final double m_cubeBackwardTopPosition = 32;
  private final double m_coneBackwardTopAngle = 232;
  private final double m_coneBackwardTopPosition = 36;
  private final double m_cubeForwardTopAngle = 120;
  private final double m_cubeForwardTopPosition = 39.6;
  private final double m_coneForwardTopAngle = 126;
  private final double m_coneForwardTopPosition = 42;
  private boolean m_auto = false;
  private boolean m_cube = false;
  private boolean m_backwards = false;

  public TopScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true),
        new ExtendRetractInternal(armExtRet,
            ArmRotationExtensionSingleton.getInstance().getPosition(), true));
  }

  public TopScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet, boolean auto,
      boolean cube, boolean backwards) {
    m_auto = auto;
    m_cube = cube;
    m_backwards = backwards;
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true),
        new ExtendRetractInternal(armExtRet,
            ArmRotationExtensionSingleton.getInstance().getPosition(), true));
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (m_auto) {
      if (m_cube) {

        if (m_backwards) {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeBackwardTopAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeBackwardTopPosition);
        } else {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeForwardTopAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeForwardTopPosition);
        }
      } else {
        if (m_backwards) {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_coneBackwardTopAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_coneBackwardTopPosition);
        } else {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_coneForwardTopAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_coneForwardTopPosition);
        }
      }
    } else {
      if ((Robot.getInstance().getOIContainer().getSubsystemController().getGrabBackwardButton())
          && (Robot.getInstance().getOIContainer().getSubsystemController().getConeButton())) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_coneBackwardTopAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_coneBackwardTopPosition);
      } else if (Robot.getInstance().getOIContainer().getSubsystemController()
          .getGrabBackwardButton()) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeBackwardTopAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeBackwardTopPosition);
      } else if (Robot.getInstance().getOIContainer().getSubsystemController().getConeButton()) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_coneForwardTopAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_coneForwardTopPosition);
      } else {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeForwardTopAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeForwardTopPosition);
      }
      Trace.getInstance().logCommandStart(this);
    }
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
