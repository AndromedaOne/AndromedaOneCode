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

public class contractArmsCommand extends CommandBase {
  public ClimberBase m_climber = Robot.getInstance().getSubsystemsContainer().getClimber();
  Config m_climberConfig = Config4905.getConfig4905().getClimberConfig();
  private double m_contractHeight = 0;

  private double m_slightlyLowerThanBarHeight = 118;

  public contractArmsCommand() {
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    m_contractHeight = m_climberConfig.getInt("contractHeight");
  }

  @Override
  public void execute() {
    if (m_climber.getLeftWinchAdjustedEncoderValue() >= m_contractHeight) {
      if (m_climber.getLeftWinchAdjustedEncoderValue() >= m_slightlyLowerThanBarHeight) {
        m_climber.driveLeftWinch(0.2);
      }

      else {
        m_climber.driveLeftWinch(1);
      }
    }

    else {
      m_climber.stopLeftWinch();
    }
    if (m_climber.getRightWinchAdjustedEncoderValue() >= m_contractHeight) {
      if (m_climber.getRightWinchAdjustedEncoderValue() >= m_slightlyLowerThanBarHeight) {
        m_climber.driveRightWinch(0.2);
      } else {
        m_climber.driveRightWinch(1);
      }
    }

    else {
      m_climber.stopRightWinch();
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.stopLeftWinch();
    m_climber.stopRightWinch();
    Trace.getInstance().logCommandStop(this);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
