/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import com.typesafe.config.*;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.subsystems.climber.ClimberBase;

public class Climb extends CommandBase {
  ClimberBase climber = Robot.getInstance().getSubsystemsContainer().getClimber();

  private int m_maxHeight;

  /**
   * Creates a new Climb.
   */
  public Climb() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climber);
    Config climberConf = Config4905.getConfig4905().getClimberConfig();
    m_maxHeight = climberConf.getInt("maxHeight");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.driveLeftWinch();
    climber.driveRightWinch();
    new BalanceClimber();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (climber.getLeftWinch().getEncoderPositionTicks() >= m_maxHeight
        || climber.getRightWinch().getEncoderPositionTicks() >= m_maxHeight);
  }
}
