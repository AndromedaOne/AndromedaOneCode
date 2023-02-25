// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.samArmRotateCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.samArmRotate.SamArmRotateBase;
import frc.robot.telemetries.Trace;

public class InitializeArmRotation extends CommandBase {
  private SamArmRotateBase m_armRotate;
  private double m_initializeAngle = 0;

  public InitializeArmRotation(SamArmRotateBase samArmRotate) {
    m_armRotate = samArmRotate;
    addRequirements(samArmRotate);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.

  // Don't know which direction to rotate the arm yet but it will always be
  // the same direction.
  @Override
  public void execute() {
    if (!m_armRotate.getInitialized()) {
      m_armRotate.rotate(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_armRotate.rotate(0);
    Trace.getInstance().logCommandStop(this);
  }

  // 0.003 = 1 degree

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_armRotate.getInitialized()) {
      return true;
    } else if ((m_armRotate.getAngle() < (m_initializeAngle + 0.003))
        && (m_armRotate.getAngle() > (m_initializeAngle - 0.003))) {
      // Want to initialize within 1 degree of initial angle.
      m_armRotate.setInitialized();
      return true;
    }
    return false;
  }
}
