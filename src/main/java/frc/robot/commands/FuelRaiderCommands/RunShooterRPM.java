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
  private ShooterBase m_bottomShooter;
  private DoubleSupplier m_bottomSetpointSupplier;
  private RunShooterWheelVelocity m_bottomShooterCommand;
  private String m_smartDashboardName = ShooterBase.getSmartDashboardShooterString();

  public RunShooterRPM(ShooterBase bottomShooter, DoubleSupplier bottomSetpoint) {
    m_bottomShooter = bottomShooter;
    m_bottomSetpointSupplier = bottomSetpoint;
    m_bottomShooterCommand = new RunShooterWheelVelocity(m_bottomShooter, m_bottomSetpointSupplier,
        Config4905.getConfig4905().getShooterConfig(), () -> false, "bottomShooter");

    addCommands(m_bottomShooterCommand);

    SmartDashboard.putNumber(m_smartDashboardName + "Set Bottom Shooter RPM", 1000);
  }

  public RunShooterRPM(ShooterBase bottomShooter) {
    this(bottomShooter, () -> (double) SmartDashboard
        .getNumber(ShooterBase.getSmartDashboardShooterString() + "Set Bottom Shooter RPM", 1000));
  }

  // Called when the command is initially scheduled.
  @Override
  public void additionalInitialize() {
    Trace.getInstance().logCommandInfo(this,
        "bottom setpoint set to " + m_bottomSetpointSupplier.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void additionalEnd(boolean interrupted) {
    m_bottomShooter.runShooter(0);
  }

  private class OnTarget implements BooleanSupplier {

    @Override
    public boolean getAsBoolean() {
      return m_bottomShooterCommand.atSetpoint();
    }
  }

  public BooleanSupplier getOnTargetSupplier() {
    return (new OnTarget());
  }

}
