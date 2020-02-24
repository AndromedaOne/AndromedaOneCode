package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class StopShooter extends CommandBase {

  private ShooterBase m_shooter;

  public StopShooter(ShooterBase shooter) {
    m_shooter = shooter;
    addRequirements(m_shooter);
  }

  @Override
  public void initialize() {
    m_shooter.setShooterWheelPower(0);
    m_shooter.setShooterSeriesPower(0);
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }
}
