// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpoints;
import frc.robot.subsystems.sbsdclimber.ClimberMode;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class MoveEndEffectorToClimberMode extends Command {
  private CoralEndEffectorRotateBase m_endEffector;
  private boolean m_isAtSetpoint = false;

  public MoveEndEffectorToClimberMode() {
    m_endEffector = Robot.getInstance().getSubsystemsContainer()
        .getSBSDCoralEndEffectorRotateBase();
    addRequirements(m_endEffector.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_endEffector.setAngleDeg(
        SBSDArmSetpoints.getInstance().getEndEffectorAngleInDeg(ArmSetpoints.CLIMBER_POSITION));
    CommandScheduler.getInstance().removeDefaultCommand(m_endEffector.getSubsystemBase());
    CommandScheduler.getInstance().setDefaultCommand(m_endEffector.getSubsystemBase(),
        new EndEffectorControlCommand(() -> SBSDArmSetpoints.ArmSetpoints.CLIMBER_POSITION, false));
    Trace.getInstance().logCommandInfo(this, "EE Angle :"
        + SBSDArmSetpoints.getInstance().getEndEffectorAngleInDeg(ArmSetpoints.CLIMBER_POSITION));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_endEffector.atSetPoint()) {
      ClimberMode.getInstance().setEndEffectorInClimberMode();
      m_endEffector.stop();
      m_isAtSetpoint = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_endEffector.stop();
    ClimberMode.getInstance().setEndEffectorInClimberMode();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isAtSetpoint;
  }
}
