package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.billClimber.BillClimberBase;

public class RunBillCimber extends Command {
  private BillClimberBase m_climber;
  private boolean m_useSmartDashboard;

  public RunBillCimber(BillClimberBase climber, boolean useSmartDashboard) {
    m_climber = climber;
    m_useSmartDashboard = useSmartDashboard;
    if (useSmartDashboard) {
      SmartDashboard.putNumber("Climber Speed", 0);

    }
    addRequirements(m_climber.getSubsystemBase());
  }

  public RunBillCimber(BillClimberBase climber) {
    this(climber, false);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void execute() {
    double speed = 0;
    if (m_useSmartDashboard) {
      speed = SmartDashboard.getNumber("Climber Speed", 0);
    } else {
      speed = Robot.getInstance().getOIContainer().getSubsystemController().getBillClimberSpeed();
    }
    m_climber.driveWinch(speed);
  }

  @Override
  public void end(boolean interrupted) {
    m_climber.stopWinch();
  }

  @Override
  public boolean isFinished() {
    return (false);
  }
}
