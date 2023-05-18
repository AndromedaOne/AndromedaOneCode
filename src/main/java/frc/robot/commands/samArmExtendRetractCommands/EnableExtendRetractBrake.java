// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmExtendRetractCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmExtRet.SamArmExtRetBase;
import frc.robot.telemetries.Trace;

public class EnableExtendRetractBrake extends CommandBase {
  private SamArmExtRetBase m_armExtendRetBase;

  /** Creates a new EnableExtendRetractBrake. */
  public EnableExtendRetractBrake(SamArmExtRetBase armExtRetBase) {
    m_armExtendRetBase = armExtRetBase;
    addRequirements(armExtRetBase.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    m_armExtendRetBase.engageArmBrake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
