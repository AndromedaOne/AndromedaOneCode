package frc.robot.commands.billthovenShooterCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.billShooter.BillShooterBase;
import frc.robot.telemetries.Trace;

//This was entirely taken from topGun so there are definitely problems
public class RunBillShooterRPM extends ParallelCommandGroup4905 {

  private BillShooterBase m_shooterWheel;
  private DoubleSupplier m_setpoint;
  private boolean m_useSmartDashboardRPM = false;
  private BooleanSupplier m_finishedCondition;
  private boolean m_finished = false;
  private RunBillShooterWheelVelocity m_shooterCommand;

  public RunBillShooterRPM(BillShooterBase shooterWheel, DoubleSupplier setpoint,
      boolean useSmartDashboardRPM, BooleanSupplier finishedCondition) {
    m_shooterWheel = shooterWheel;
    m_setpoint = setpoint;
    m_useSmartDashboardRPM = useSmartDashboardRPM;
    m_finishedCondition = finishedCondition;

    m_shooterCommand = new RunBillShooterWheelVelocity(m_shooterWheel, m_setpoint,
        Config4905.getConfig4905().getBillShooterConfig(), m_finishedCondition);

    if (useSmartDashboardRPM) {
      m_finishedCondition = new FinishedConditionSupplier();
    }
    addCommands(m_shooterCommand);
  }

  public RunBillShooterRPM(BillShooterBase shooterWheel, DoubleSupplier setpoint) {
    this(shooterWheel, setpoint, false, () -> false);
  }

  public RunBillShooterRPM(BillShooterBase shooterWheel) {
    this(shooterWheel, () -> 0, true, () -> false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (m_useSmartDashboardRPM) {
      m_setpoint = () -> SmartDashboard.getNumber("Set Shooter RPM", 1000);
      m_finished = false;
    }
    System.out.println("Setpoint Set To" + m_setpoint);
    Trace.getInstance().logCommandInfo(this, "Addiitonal stuff ");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {
    m_shooterWheel.setShooterWheelPower(0);
    m_finished = true;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinishedAdditional() {
    return m_finishedCondition.getAsBoolean();
  }

  private class FinishedConditionSupplier implements BooleanSupplier {

    @Override
    public boolean getAsBoolean() {
      return m_finished;
    }

  }

  public BooleanSupplier atSetpoint() {
    if (m_shooterCommand.atSetpoint()) {
      System.out
          .println("Shooter Wheel At Setpoint WheelSpeed: " + m_shooterWheel.getShooterWheelRpm());
    }
    return () -> (m_shooterCommand.atSetpoint());
  }
}
