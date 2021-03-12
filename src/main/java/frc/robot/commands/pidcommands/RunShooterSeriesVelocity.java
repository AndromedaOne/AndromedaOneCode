package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class RunShooterSeriesVelocity extends CommandBase {

  private ShooterBase m_shooter;
  private double m_setpoint = 0;
  private static Config m_pidConfig;
  private static Config m_shooterConfig;
  private final double kControllerScale;

  /**
   * @param shooter
   * @param m_subsystemController Requires a controller to allow the subsystem
   *                              driver to tune the PID setpoint via the
   *                              controller
   * @param setpoint
   */
  public RunShooterSeriesVelocity(ShooterBase shooter, double setpoint) {
    // PID Controller

    kControllerScale = Config4905.getConfig4905().getCommandConstantsConfig()
        .getDouble("RunShooterSeriesVelocity.shooterwheeljoystickscale");
    m_shooter = shooter;
    m_setpoint = setpoint;
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
  }

  @Override
  public void execute() {
    m_shooter.setShooterSeriesPower(0.875);
    super.execute();
  }

  @Override
  public void end(boolean interrupt) {
    m_shooter.setShooterSeriesPower(0);
    Trace.getInstance().logCommandStop(this);
  }

  private static PIDController4905 createPIDController() {
    m_pidConfig = Config4905.getConfig4905().getCommandConstantsConfig();

    double kp = m_pidConfig.getDouble("runshooterseriesvelocity.p");
    double ki = m_pidConfig.getDouble("runshooterseriesvelocity.i");
    double kd = m_pidConfig.getDouble("runshooterseriesvelocity.d");

    return new PIDController4905("ShooterSeriesWheelPID", kp, ki, kd, 0);
  }

  private SimpleMotorFeedforward createFeedForward() {
    double ks = m_pidConfig.getDouble("runshooterseriesvelocity.s");
    double kv = m_pidConfig.getDouble("runshooterseriesvelocity.v");

    return new SimpleMotorFeedforward(ks, kv);
  }

}