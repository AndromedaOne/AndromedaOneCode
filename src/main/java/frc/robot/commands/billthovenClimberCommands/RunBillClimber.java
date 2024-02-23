package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.subsystems.billClimber.BillClimberBase;

public class RunBillClimber extends Command {
  private BillClimberBase m_climber;
  private boolean m_useSmartDashboard;
  private DriveController m_driveController;

  public RunBillClimber(BillClimberBase climber, boolean useSmartDashboard) {
    m_climber = climber;
    m_useSmartDashboard = useSmartDashboard;

    if (useSmartDashboard) {
      SmartDashboard.putNumber("Climber Speed", 0);

    }
    addRequirements(m_climber.getSubsystemBase());
  }

  public RunBillClimber(BillClimberBase climber) {
    this(climber, false);
  }

  @Override
  public void initialize() {
    m_driveController = Robot.getInstance().getOIContainer().getDriveController();
  }

  @Override
  public void execute() {
    double speed = 0;
    if (m_useSmartDashboard) {
      speed = SmartDashboard.getNumber("Climber Speed", 0);
    } else {
      speed = Robot.getInstance().getOIContainer().getSubsystemController().getBillClimberSpeed();
    }
    speed = Math.pow(speed, 5);
    m_climber.driveWinch(speed, m_driveController.getClimberOverrideTrigger());
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
