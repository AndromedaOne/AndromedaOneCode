package frc.robot.commands.billthovenFeederCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billFeeder.BillFeederBase;

public class StopBillFeeder extends Command {
  private BillFeederBase m_feeder;

  /** Creates a new StopFeeder. */
  public StopBillFeeder(BillFeederBase feeder) {
    m_feeder = feeder;
    addRequirements(m_feeder.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_feeder.stopBillFeeder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

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