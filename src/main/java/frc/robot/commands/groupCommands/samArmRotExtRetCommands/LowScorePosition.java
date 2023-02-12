// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;

public class LowScorePosition extends CommandBase {
  /** Creates a new LowPositionScore. */
  private final double m_lowAngle = 0;
  private final double m_lowPosition = 0;

  public LowScorePosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRet) {
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ArmRotationExtensionSingleton.getInstance().setAngle(m_lowAngle);
    ArmRotationExtensionSingleton.getInstance().setPosition(m_lowPosition);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
