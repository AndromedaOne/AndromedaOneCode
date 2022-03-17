// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climberCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.subsystems.climber.ClimberBase;
import frc.robot.telemetries.Trace;

public class extendArmsCommand extends CommandBase {
  public ClimberBase m_climber = Robot.getInstance().getSubsystemsContainer().getClimber();
  Config m_climberConfig = Config4905.getConfig4905().getClimberConfig();
  private double m_maxExtendHeight = 0;

  public extendArmsCommand() {
    addRequirements(m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    m_maxExtendHeight = m_climberConfig.getInt("maxExtendHeight");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_climber.getBackLeftWinchAdjustedEncoderValue() >= m_maxExtendHeight) {
      m_climber.stopBackLeftWinch();
    } else {
      m_climber.unwindBackLeftWinch();
    }

    if (m_climber.getBackRightWinchAdjustedEncoderValue() >= m_maxExtendHeight) {
      m_climber.stopBackRightWinch();
    } else {
      m_climber.unwindBackRightWinch();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.stopBackLeftWinch();
    m_climber.stopBackRightWinch();
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (m_climber.getBackLeftWinchAdjustedEncoderValue() >= m_maxExtendHeight
        && m_climber.getBackRightWinchAdjustedEncoderValue() >= m_maxExtendHeight);
  }

}
