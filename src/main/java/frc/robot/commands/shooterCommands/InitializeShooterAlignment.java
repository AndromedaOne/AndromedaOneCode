// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.telemetries.Trace;

public class InitializeShooterAlignment extends CommandBase {
  private ShooterAlignmentBase m_shooterAlignment;
  private boolean m_rotateUp = true;

  /** Creates a new InitializeShooterAlignment. */
  public InitializeShooterAlignment(ShooterAlignmentBase shooterAlignment) {
    m_shooterAlignment = shooterAlignment;
    addRequirements(shooterAlignment);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!m_shooterAlignment.getInitialized()) {
      // Rotating shooter up until the limit switch is turned off
      if (m_rotateUp) {
        m_shooterAlignment.rotateShooter(0.3);
      } else {
        m_shooterAlignment.rotateShooter(-0.3);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooterAlignment.rotateShooter(0);
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_shooterAlignment.getInitialized()) {
      return true;
    } else if (m_rotateUp && !m_shooterAlignment.atBottomLimitSwitch()) {
      m_rotateUp = false;
      return false;
    } else if (!m_rotateUp && m_shooterAlignment.atBottomLimitSwitch()) {
      m_shooterAlignment.setOffset(m_shooterAlignment.getAngle());
      m_shooterAlignment.setInitialized();
      return true;
    } else {
      return false;
    }
  }
}
