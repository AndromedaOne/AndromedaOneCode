// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climberCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.subsystems.climber.ClimberBase;
import frc.robot.telemetries.Trace;

public class MoveClimberArms extends CommandBase {
  private ClimberBase m_climber;
  private DriveController m_controller;
  Config m_climberConfig = Config4905.getConfig4905().getClimberConfig();
  private double m_maxExtendHeight = 0;
  private double m_contractHeight = 0;

  public MoveClimberArms(ClimberBase climber) {
    m_climber = climber;
    m_controller = Robot.getInstance().getOIContainer().getDriveController();
    m_maxExtendHeight = m_climberConfig.getInt("maxExtendHeight");
    m_contractHeight = m_climberConfig.getInt("contractHeight");
    addRequirements(climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_controller.getExtendTriggerValue() > 0) {
      if (m_climber.getLeftWinchAdjustedEncoderValue() < m_maxExtendHeight) {
        m_climber.driveLeftWinch(-m_controller.getExtendTriggerValue());
      } else {
        m_climber.stopLeftWinch();
      }
      if (m_climber.getRightWinchAdjustedEncoderValue() < m_maxExtendHeight) {
        m_climber.driveRightWinch(-m_controller.getExtendTriggerValue());
      } else {
        m_climber.stopRightWinch();
      }
    } else if (m_controller.getContractTriggerValue() > 0) {
      if (m_climber.getLeftWinchAdjustedEncoderValue() > m_contractHeight) {
        m_climber.driveLeftWinch(m_controller.getContractTriggerValue());
      } else {
        m_climber.stopLeftWinch();
      }
      if (m_climber.getRightWinchAdjustedEncoderValue() > m_contractHeight) {
        m_climber.driveRightWinch(m_controller.getContractTriggerValue());
      } else {
        m_climber.stopRightWinch();
      }
    } else {
      m_climber.stopLeftWinch();
      m_climber.stopRightWinch();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
