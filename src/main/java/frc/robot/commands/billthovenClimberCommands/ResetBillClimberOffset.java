package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billClimber.BillClimberBase;

public class ResetBillClimberOffset extends Command {
  private BillClimberBase m_climber;

  public ResetBillClimberOffset(BillClimberBase climber) {
    m_climber = climber;
    addRequirements(m_climber.getSubsystemBase());
  }

  @Override
  public void initialize() {
    m_climber.resetOffset();
  }

  @Override
  public void execute() {

  }

  @Override
  public void end(boolean interrupted) {

  }

  @Override
  public boolean isFinished() {
    return true;
  }

}