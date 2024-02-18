package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.billClimber.BillClimberBase;

public class RunBillCimber extends Command {
  private BillClimberBase m_climber;
  private double m_speed;
  private boolean m_useSmartDashboard;
  private boolean m_needToEnd;
  private boolean m_readJoystick;

  public RunBillCimber(BillClimberBase climber, boolean needToEnd, double speed,
      boolean useSmartDashboard, boolean readJoystick) {
    m_climber = climber;
    m_speed = speed;
    m_useSmartDashboard = useSmartDashboard;
    m_needToEnd = needToEnd;
    m_readJoystick = readJoystick;
    if (useSmartDashboard) {
      SmartDashboard.putNumber("Climber Speed", 0);

    }
    addRequirements(m_climber.getSubsystemBase());
  }

  public RunBillCimber(BillClimberBase climber, boolean needToEnd, double speed) {
    this(climber, needToEnd, speed, false, false);
  }

  public RunBillCimber(BillClimberBase climber, boolean needToEnd) {
    this(climber, needToEnd, 0, false, true);
  }

  @Override
  public void initialize() {
    if (m_useSmartDashboard) {
      m_speed = SmartDashboard.getNumber("Climber Speed", 0);
    }
    if ((m_speed != 0) && (!m_readJoystick)) {
      m_climber.setWinchBrakeMode(false);
    }

    m_climber.resetFinished();
  }

  @Override
  public void execute() {
    if (m_readJoystick) {
      m_speed = Robot.getInstance().getOIContainer().getSubsystemController().getBillClimberSpeed();
    }
    m_climber.driveWinch(m_speed);
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    m_climber.setWinchBrakeMode(true);
  }

  @Override
  public boolean isFinished() {
    return (m_needToEnd && m_climber.isFinished());
  }
}
