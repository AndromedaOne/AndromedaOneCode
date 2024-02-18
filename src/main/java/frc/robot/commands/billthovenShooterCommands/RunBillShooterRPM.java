package frc.robot.commands.billthovenShooterCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.billShooter.BillShooterBase;

//This was entirely taken from topGun so there are definitely problems
public class RunBillShooterRPM extends ParallelCommandGroup4905 {

  private BillShooterBase m_shooterWheel;
  private SetPointSupplier m_setpoint;
  private boolean m_useSmartDashboardRPM = false;
  private RunBillShooterWheelVelocity m_shooterCommand;

  public RunBillShooterRPM(BillShooterBase shooterWheel, double setpoint,
      boolean useSmartDashboardRPM) {
    m_shooterWheel = shooterWheel;
    m_useSmartDashboardRPM = useSmartDashboardRPM;
    m_setpoint = new SetPointSupplier(setpoint);

    m_shooterCommand = new RunBillShooterWheelVelocity(m_shooterWheel, m_setpoint,
        Config4905.getConfig4905().getBillShooterConfig(), () -> false);

    addCommands(m_shooterCommand);
    SmartDashboard.putNumber("Set Shooter RPM", 1000);
  }

  public RunBillShooterRPM(BillShooterBase shooterWheel, double setpoint) {
    this(shooterWheel, setpoint, false);
  }

  public RunBillShooterRPM(BillShooterBase shooterWheel) {
    this(shooterWheel, 0, true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (m_useSmartDashboardRPM) {
      m_setpoint.setSetpoint(SmartDashboard.getNumber("Set Shooter RPM", 1000));
    }
    System.out.println("Setpoint Set To" + m_setpoint.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {
    m_shooterWheel.setShooterWheelPower(0);
  }

  private class SetPointSupplier implements DoubleSupplier {
    private double m_setpointSupplied = 0;

    public SetPointSupplier(double setPoint) {
      m_setpointSupplied = setPoint;
    }

    @Override
    public double getAsDouble() {
      return m_setpointSupplied;
    }

    public void setSetpoint(double value) {
      m_setpointSupplied = value;
    }
  }

  private class OnTarget implements BooleanSupplier {

    @Override
    public boolean getAsBoolean() {
      return m_shooterCommand.atSetpoint();
    }
  }

  public BooleanSupplier getOnTargetSupplier() {
    return (new OnTarget());
  }

}
