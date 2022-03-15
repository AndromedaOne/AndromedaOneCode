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
  private double m_rightHeight = 0;
  private double m_leftHeight = 0;

  public contractArmsCommand() {
    addRequirements(m_climber);
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    m_contractHeight = m_climberConfig.getInt("contractHeight");
    m_rightHeight = (m_climber.getBackRightWinch().getEncoderPositionTicks() - m_contractHeight);
    System.out.println("RIghtHeight " + m_rightHeight);
    m_leftHeight = (m_climber.getBackLeftWinch().getEncoderPositionTicks() - m_contractHeight);
    System.out.println("LeftHeight " + m_leftHeight);
  }

  @Override
  public void execute() {
    m_climber.driveBackLeftWinch();
    m_climber.driveBackRightWinch();
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.stopBackLeftWinch();
    m_climber.stopBackRightWinch();
    Trace.getInstance().logCommandStop(this);
  }

  @Override
  public boolean isFinished() {
    System.out
        .println("Left Encoder Position " + m_climber.getBackLeftWinch().getEncoderPositionTicks());
    System.out.println(
        "Right Encoder Position " + m_climber.getBackRightWinch().getEncoderPositionTicks());
    return (m_climber.getBackLeftWinch().getEncoderPositionTicks() <= m_leftHeight
        || m_climber.getBackRightWinch().getEncoderPositionTicks() <= m_rightHeight
        || m_climber.backLeftWinchAtBottomLimitSwitch()
        || m_climber.backRightWinchAtBottomLimitSwitch());
  }
}
