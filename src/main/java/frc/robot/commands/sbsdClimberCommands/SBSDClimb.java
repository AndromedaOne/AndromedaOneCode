// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdclimber.ClimberMode;
import frc.robot.subsystems.sbsdclimber.SBSDClimberBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SBSDClimb extends Command {
  /** Creates a new SBSDClimb. */
  private SBSDClimberBase m_climber;
  private boolean m_inReverse = false;
  private boolean m_usingSmartDashboard = false;

  public SBSDClimb(boolean usingSmartDasboard, boolean inReverse) {
    m_climber = Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase();
    addRequirements(m_climber.getSubsystemBase());
    m_usingSmartDashboard = usingSmartDasboard;
    m_inReverse = inReverse;
  }

  public SBSDClimb(boolean inReverse) {
    m_climber = Robot.getInstance().getSubsystemsContainer().getSBSDClimberBase();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_climber.getSubsystemBase());
    m_inReverse = inReverse;
    m_usingSmartDashboard = false;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (ClimberMode.getInstance().getInClimberMode() || m_usingSmartDashboard) {
      if (!m_inReverse) {
        m_climber.climb();
      } else {
        m_climber.reverseClimb();
      }
    } else {
      m_climber.stop();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (!ClimberMode.getInstance().getInClimberMode() && !m_usingSmartDashboard) {
      m_climber.stop();
      return true;
    }
    return false;
  }
}
