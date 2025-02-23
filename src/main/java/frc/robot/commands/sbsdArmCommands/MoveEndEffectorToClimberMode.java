// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.commands.sbsdArmCommands.SBSDArmSetpoints.ArmSetpoints;
import frc.robot.subsystems.sbsdclimber.ClimberMode;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

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
        SBSDArmSetpoints.getInstance().getArmAngleInDeg(ArmSetpoints.CLIMBER_POSITION));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_endEffector.atSetPoint()) {
      ClimberMode.getInstance().setEndEffectorInClimberMode();
      m_endEffector.stop();
      m_isAtSetpoint = true;
    } else {
      m_endEffector.calculateSpeed();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_isAtSetpoint;
  }
}
