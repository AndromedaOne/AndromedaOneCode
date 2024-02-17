package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.billClimber.BillClimberBase;
import frc.robot.telemetries.Trace;

public class RunBillCimber extends Command {
  private BillClimberBase m_climber;
  private double m_speed;
  private boolean m_useSmartDashboard;

  public RunBillCimber(BillClimberBase climber, boolean needToEnd, double speed,
      boolean useSmartDashboard) {
    m_climber = climber;
    m_speed = speed;
    m_useSmartDashboard = useSmartDashboard;
    if (useSmartDashboard) {
      SmartDashboard.putNumber("Climber Speed", 0);

    }
    addRequirements(m_climber.getSubsystemBase());
  }

  public RunBillCimber(BillClimberBase climber, boolean needToEnd, double speed) {
    this(climber, needToEnd, speed, false);
  }

  @Override
  public void initialize() {
    m_climber.setWinchBrakeMode(false);
    if (m_useSmartDashboard) {
      m_speed = SmartDashboard.getNumber("Climber Speed", 0);
    }
  }

  @Override
  public void execute() {
    m_climber.driveWinch(m_speed);
    Trace.getInstance().logCommandInfo(this, "excuting Bill climber " + m_speed);
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    m_climber.setWinchBrakeMode(true);
  }

  @Override
  public boolean isFinished() {
    return m_climber.isFinished();
  }
}
