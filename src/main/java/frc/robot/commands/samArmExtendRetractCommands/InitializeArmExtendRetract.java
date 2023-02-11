// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.telemetries.Trace;

public class InitializeArmExtendRetract extends CommandBase {
  private SamArmExtRetBase m_armExtRet;

  public InitializeArmExtendRetract(SamArmExtRetBase samArmExtRet) {
    m_armExtRet = samArmExtRet;
    addRequirements(samArmExtRet);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!m_armExtRet.getInitialized() && !m_armExtRet.getRetractLimitSwitchState()) {
      m_armExtRet.extendRetract(-0.1);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_armExtRet.extendRetract(0);
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_armExtRet.getInitialized()) {
      return true;
    } else if (m_armExtRet.getRetractLimitSwitchState()) {
      m_armExtRet.setInitialized();
      return true;
    }
    return false;
  }
}
