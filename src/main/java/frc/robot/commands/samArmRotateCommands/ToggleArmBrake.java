// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmRotateCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmRotate.ArmAngleBrakeState;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class ToggleArmBrake extends CommandBase {
  /** Creates a new ToggleArmBrake. */
  protected SamArmRotateBase m_armRotateBase;

  public ToggleArmBrake(SamArmRotateBase armRotateBase) {
    m_armRotateBase = armRotateBase;
    addRequirements(m_armRotateBase);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    if (m_armRotateBase.getState() == ArmAngleBrakeState.ENGAGEARMBRAKE) {
      m_armRotateBase.disengageArmBrake();
    } else if (m_armRotateBase.getState() == ArmAngleBrakeState.DISENGAGEARMBRAKE) {
      m_armRotateBase.engageArmBrake();
    }
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
    return true;
  }
}
