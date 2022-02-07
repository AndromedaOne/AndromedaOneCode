// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climberCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.subsystems.climber.ClimberBase;

public class extendArmsCommand extends CommandBase {
  public ClimberBase m_climber = Robot.getInstance().getSubsystemsContainer().getClimber();
  Config m_climberConfig = Config4905.getConfig4905().getClimberConfig();
  private int m_extendHeight = 0;

  public extendArmsCommand() {

    m_extendHeight = m_climberConfig.getInt("extendHeight");
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climber.driveBackLeftWinch();

    m_climber.driveBacktRightWinch();
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
