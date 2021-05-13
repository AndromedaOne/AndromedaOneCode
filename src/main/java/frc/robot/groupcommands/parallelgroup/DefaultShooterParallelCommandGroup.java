package frc.robot.groupcommands.parallelgroup;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.commands.pidcommands.RunShooterSeriesVelocity;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.subsystems.shooter.ShooterBase;

public class DefaultShooterParallelCommandGroup extends ParallelCommandGroup {
  private double m_shooterIdleSpeed;
  private double m_seriesIdleSpeed;
  private ShooterBase m_shooter;

  /**
   * @param shooter
   * @param controller Requires a controller to allow the subsystem driver to tune
   *                   the PID setpoint via the controller
   */
  public DefaultShooterParallelCommandGroup(ShooterBase shooter) {
    Config constConfig = Config4905.getConfig4905().getCommandConstantsConfig();
    m_shooterIdleSpeed = constConfig.getDouble("DefaultShooterParallelCommandGroup.shooteridlespeed");
    m_seriesIdleSpeed = constConfig.getDouble("DefaultShooterParallelCommandGroup.seriesidlespeed");
    m_shooter = shooter;
    addRequirements(shooter);

    addCommands(new RunShooterSeriesVelocity(m_shooter, m_seriesIdleSpeed),
        new RunShooterWheelVelocity(m_shooter, () -> m_shooterIdleSpeed));
  }

  @Override
  public void initialize() {
    // When this command is running the shooter is always idle
    m_shooter.setShooterIsIdle(true);
    // When the shooter is idle the hood will always be close
    // m_shooter.closeShooterHood();
  }

  @Override
  public void execute() {
    super.execute();
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    // When this command isnt running the shooter is not idle
    m_shooter.setShooterIsIdle(false);
  }

}
