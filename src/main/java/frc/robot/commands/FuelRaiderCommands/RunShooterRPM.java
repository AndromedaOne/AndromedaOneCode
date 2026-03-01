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
  private RunShooterWheelVelocity m_shooterCommand;
  private String m_smartDashboardName = ShooterBase.getSmartDashboardShooterString();

  public RunShooterRPM(ShooterBase shooter, DoubleSupplier setpoint) {
    m_shooter = shooter;
    m_setpoint = setpoint;
    m_shooterCommand = new RunShooterWheelVelocity(m_shooter, m_setpoint,
        Config4905.getConfig4905().getShooterConfig(), () -> false);

    addCommands(m_shooterCommand);
    SmartDashboard.putNumber(m_smartDashboardName + "Set Shooter RPM", 1000);
  }

  public RunShooterRPM(ShooterBase shooter) {
    this(shooter, () -> (double) SmartDashboard
        .getNumber(ShooterBase.getSmartDashboardShooterString() + "Set Shooter RPM", 1000));
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    Trace.getInstance().logCommandInfo(this, "setpoint set to " + m_setpoint.getAsDouble());
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
