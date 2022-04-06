// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.telemetries.Trace;

public class EndgameMoveShooterAlignment extends CommandBase {
  private ShooterAlignmentBase m_shooterAlignmentBase;
  private final double m_minimumEndgameAngle = 25;
  private final double m_maximumEndgameAngle = 50;

  /** Creates a new EndgameMoveShooterAlignment. */
  public EndgameMoveShooterAlignment(ShooterAlignmentBase shooterAlignmentBase) {
    addRequirements(shooterAlignmentBase);
    m_shooterAlignmentBase = shooterAlignmentBase;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
   double stickValue = Robot.getInstance().getOIContainer()
   .getSubsystemController().getEndgameShooterAlignmentStick();
    if ((m_shooterAlignmentBase.getAngle() > m_minimumEndgameAngle) && (stickValue < 0)) {
      m_shooterAlignmentBase.rotateShooter(stickValue);
    }
    if ((m_shooterAlignmentBase.getAngle() < m_maximumEndgameAngle) && (stickValue > 0)) {
      m_shooterAlignmentBase.rotateShooter(stickValue);
    }
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
