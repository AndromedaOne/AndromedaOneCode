package frc.robot.commands.FuelRaiderCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.rewrittenWPIclasses.ParallelCommandGroup4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

//This was entirely taken from topGun so there are definitely problems
public class RunShooterRPM extends ParallelCommandGroup4905 {

  // we probably want another command to call this and use the interpolation
  // tables... yeah we do
  private ShooterBase m_shooter;
  private DoubleSupplier m_setpoint;
  private boolean m_useSmartDashboardRPM = false;
  private RunShooterWheelVelocity m_shooterCommand;
  private String m_smartDashboardName = ShooterBase.getSmartDashboardShooterString();

  public RunShooterRPM(ShooterBase shooter, DoubleSupplier setpoint, boolean useSmartDashboardRPM) {
    m_shooter = shooter;
    m_useSmartDashboardRPM = useSmartDashboardRPM;
    m_setpoint = setpoint;

    m_shooterCommand = new RunShooterWheelVelocity(m_shooter, m_setpoint,
        Config4905.getConfig4905().getShooterConfig(), () -> false);

    addCommands(m_shooterCommand);
    SmartDashboard.putNumber(m_smartDashboardName + "Set Shooter RPM", 1000);
  }

  public RunShooterRPM(ShooterBase shooter, DoubleSupplier setpoint) {
    this(shooter, setpoint, false);
  }

  public RunShooterRPM(ShooterBase shooter) {
    this(shooter, () -> 0, true);
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    if (m_useSmartDashboardRPM) {
      m_setpoint = () -> SmartDashboard.getNumber(m_smartDashboardName + "Set Shooter RPM", 1000);
    }
    Trace.getInstance().logCommandInfo(this, "setpoint set to " + m_setpoint);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {
    m_shooter.runShooter(0);
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
