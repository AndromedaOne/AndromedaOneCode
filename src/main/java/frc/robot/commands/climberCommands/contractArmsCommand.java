// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climberCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.subsystems.climber.ClimberBase;

public class contractArmsCommand extends CommandBase {
  public ClimberBase m_climber = Robot.getInstance().getSubsystemsContainer().getClimber();
  Config m_climberConfig = Config4905.getConfig4905().getClimberConfig();
  private int m_contractHeight = 0;

  public contractArmsCommand() {
    m_contractHeight = m_climberConfig.getInt("contractHeight");
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    m_climber.unwindBackLeftWinch();
    m_climber.unwindBackRightWinch();
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return (m_climber.getBackLeftWinch().getEncoderPositionTicks() >= m_contractHeight
        || m_climber.getBackLeftWinch().getEncoderPositionTicks() >= m_contractHeight);
  }
}
