// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.samArmRotExtRetCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;

public class SubstationPickupPosition extends CommandBase {
  /** Creates a new SubstationPickupPosition. */
  private final double m_substationAngle = 0;
  private final double m_substationPosition = 0;

  public SubstationPickupPosition(SamArmRotateBase armRotate, SamArmExtRetBase armExtRotate) {
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ArmRotationExtensionSingleton.getInstance().setAngle(m_substationAngle);
    ArmRotationExtensionSingleton.getInstance().setPosition(m_substationPosition);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
