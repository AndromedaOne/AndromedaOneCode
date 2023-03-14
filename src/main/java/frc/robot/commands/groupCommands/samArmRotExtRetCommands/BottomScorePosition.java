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

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class BottomScorePosition extends SequentialCommandGroup4905 {

  private final double m_bottomAngle = 90;
  private final double m_bottomPosition = 0;
  private final double m_backwardBottomAngle = 270;
  private final double m_backwardBottomPosition = 0;
  private boolean m_auto = false;
  private boolean m_cube = false;
  private boolean m_backwards = false;

  public BottomScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
    addCommands(new SequentialCommandGroup4905(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true),
        new ExtendRetractInternal(armExtRet,
            ArmRotationExtensionSingleton.getInstance().getPosition(), true)));

  }

  public BottomScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet, boolean auto,
      boolean cube, boolean backwards) {
    addCommands(
        new RotateArm(armRotate, ArmRotationExtensionSingleton.getInstance().getAngle(), true));
    new ExtendRetractInternal(armExtRet, ArmRotationExtensionSingleton.getInstance().getPosition(),
        true);
    m_auto = auto;
    m_cube = cube;
    m_backwards = backwards;
  }

  @Override
  public void additionalInitialize() {
    if (m_auto) {

      if (m_backwards) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_backwardBottomAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_backwardBottomPosition);

      } else {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_bottomAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_bottomPosition);
      }

    } else {
      if (Robot.getInstance().getOIContainer().getSubsystemController().getGrabBackwardButton()) {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_backwardBottomAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_backwardBottomPosition);
      } else {
        ArmRotationExtensionSingleton.getInstance().setAngle(m_bottomAngle);
        ArmRotationExtensionSingleton.getInstance().setPosition(m_bottomPosition);
      }
    }
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
