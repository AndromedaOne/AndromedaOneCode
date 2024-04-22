package frc.robot.commands.billthovenShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billShooter.BillShooterBase;

public class StopBillShooter extends Command {
  private BillShooterBase m_shooterWheel;

  /** Creates a new DefaultShooter. */
  public StopBillShooter(BillShooterBase shooterWheel) {
    m_shooterWheel = shooterWheel;
    addRequirements(shooterWheel.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooterWheel.setShooterWheelPower(0);
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
