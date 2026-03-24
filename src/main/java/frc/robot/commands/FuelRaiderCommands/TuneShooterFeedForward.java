package frc.robot.commands.FuelRaiderCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class TuneShooterFeedForward extends Command {
  /**
   * Creates a new TuneBillShooterFeedForward.
   */
  private ShooterBase m_shooter;
  private String m_shooterName;
  private String m_smartDashboardName = ShooterBase.getSmartDashboardShooterString();

  public TuneShooterFeedForward(ShooterBase shooter, String shooterName) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shooter = shooter;
    m_shooterName = shooterName;
    m_smartDashboardName = m_smartDashboardName.concat(m_shooterName + "/");
    SmartDashboard.putNumber(m_smartDashboardName + "Feed Forward Value", 0.00025);
    SmartDashboard.putNumber(m_smartDashboardName + "Feed Forward p Value", 0.0001);
    SmartDashboard.putNumber(m_smartDashboardName + "Feed Forward ShooterRPMTarget", 3000);
    System.out.println("end constructor of TuneShooterFeedForward");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("TuneShooterInitialize");
    double feedForward = SmartDashboard.getNumber(m_smartDashboardName + "Feed Forward Value",
        0.00025);
    Trace.getInstance().logCommandInfo(this, "Feed forward: " + feedForward);
    double PValue = SmartDashboard.getNumber(m_smartDashboardName + "Feed Forward p Value", 0.0001);
    double shootRPM = SmartDashboard
        .getNumber(m_smartDashboardName + "Feed Forward ShooterRPMTarget", 3000);
    System.out.println("Scheduling RunShooterWheelVelocity");
    CommandScheduler.getInstance()
        .schedule(new RunShooterWheelVelocity(m_shooter, () -> shootRPM, true, feedForward, PValue,
            Config4905.getConfig4905().getShooterConfig(), () -> false, m_shooterName));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
