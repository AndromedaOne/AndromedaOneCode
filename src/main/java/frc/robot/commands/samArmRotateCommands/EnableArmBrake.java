// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmRotateCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class EnableArmBrake extends CommandBase {
  /** Creates a new ToggleArmBrake. */
  protected SamArmRotateBase m_armRotateBase;

  public EnableArmBrake(SamArmRotateBase armRotateBase) {
    m_armRotateBase = armRotateBase;
    addRequirements(m_armRotateBase);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_armRotateBase.engageArmBrake();
    m_armRotateBase.stop();

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_armRotateBase.disengageArmBrake();
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
