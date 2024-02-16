package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billClimber.BillClimberBase;

public class RunBillCimber extends Command {
  private BillClimberBase m_climber;
  private double m_speed;

  public RunBillCimber(BillClimberBase climber, boolean needToEnd, double speed) {
    m_climber = climber;
    m_speed = speed;
    addRequirements(m_climber.getSubsystemBase());
  }

  @Override
  public void initialize() {
    m_climber.setWinchBrakeMode(false);

  }

  @Override
  public void execute() {
    m_climber.driveWinch(m_speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.setWinchBrakeMode(true);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
