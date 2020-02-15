package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.ClimberBase;

public class SetWinchSpeed extends CommandBase {
  private ClimberBase m_climber;
  private double m_speed;

  public SetWinchSpeed(ClimberBase climber, double speed) {
    m_climber = climber;
    m_speed = speed;
    addRequirements(m_climber);
  }

  public void execute() {
    m_climber.adjustWinch(m_speed);
  }

  public void end() {
    m_climber.stopLeftWinch();
    m_climber.stopRightWinch();
  }

  public boolean isFinished() {
    return false;
  }
}