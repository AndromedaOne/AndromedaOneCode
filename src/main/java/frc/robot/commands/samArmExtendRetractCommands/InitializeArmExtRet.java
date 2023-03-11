// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase.RetractLimitSwitchState;

public class InitializeArmExtRet extends CommandBase {
  private SamArmExtRetBase m_armExtRetBase;

  /** Creates a new InitializeArmExtRet. */
  public InitializeArmExtRet(SamArmExtRetBase samArmExtRet) {
    m_armExtRetBase = samArmExtRet;
    addRequirements(m_armExtRetBase);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_armExtRetBase.retractArmInitialize();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_armExtRetBase.setZeroOffset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_armExtRetBase.getRetractLimitSwitchState() == RetractLimitSwitchState.CLOSED;
  }
}
