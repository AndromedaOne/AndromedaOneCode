/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.climber.ClimberBase;

public class DriveLeftWinch extends CommandBase {
  private double m_speed;
  private ClimberBase m_climber = Robot.getInstance().getSubsystemsContainer().getClimber();
  /**
   * Creates a new DriveLeftWinch.
   */
  public DriveLeftWinch(double speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_speed = speed;
    addRequirements(m_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_climber.driveLeftWinch(m_speed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_climber.stopLeftWinch();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
