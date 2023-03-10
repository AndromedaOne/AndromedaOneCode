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

public class OffFloorPickupPosition extends SequentialCommandGroup4905 {
  /** Creates a new LowPositionScore. */
  private final double m_cubeBackwardLowAngle = 289;
  private final double m_cubeBackwardLowPosition = 0;
  private final double m_coneBackwardLowAngle = 289;
  private final double m_coneBackwardLowPosition = 0;
  private final double m_cubeForwardLowAngle = 69.9;
  private final double m_cubeForwardLowPosition = 5;
  private final double m_coneForwardLowAngle = 69.9;
  private final double m_coneForwardLowPosition = 5;
  private boolean m_auto = false;
  private boolean m_cube = false;
  private boolean m_backwards = false;

  public OffFloorPickupPosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
    new ExtendRetract(armExtRet, ArmRotationExtensionSingleton.getInstance().getPosition(), true);
  }

  public OffFloorPickupPosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet,
      boolean auto, boolean cube, boolean backwards) {
    m_auto = auto;
    m_cube = cube;
    m_backwards = backwards;
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
    new ExtendRetract(armExtRet, ArmRotationExtensionSingleton.getInstance().getPosition(), true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (m_auto) {
      if (m_cube) {

        if (m_backwards) {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeBackwardLowAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeBackwardLowPosition);
        } else {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeForwardLowAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeForwardLowPosition);
        }
      } else {
        if (m_backwards) {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_coneBackwardLowAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_coneBackwardLowPosition);
        } else {
          ArmRotationExtensionSingleton.getInstance().setAngle(m_coneForwardLowAngle);
          ArmRotationExtensionSingleton.getInstance().setPosition(m_coneForwardLowPosition);
        }
      }
    } else {
      if ((Robot.getInstance().getOIContainer().getSubsystemController().getGrabBackwardButton())
          && (Robot.getInstance().getOIContainer().getSubsystemController().getConeButton())) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_coneBackwardLowAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_coneBackwardLowPosition);
      } else if (Robot.getInstance().getOIContainer().getSubsystemController()
          .getGrabBackwardButton()) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeBackwardLowAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeBackwardLowPosition);
      } else if (Robot.getInstance().getOIContainer().getSubsystemController().getConeButton()) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_coneForwardLowAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_coneForwardLowPosition);
      } else {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_cubeForwardLowAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_cubeForwardLowPosition);
      }

    }

    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
