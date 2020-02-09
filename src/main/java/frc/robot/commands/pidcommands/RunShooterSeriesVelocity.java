package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Config4905;
import frc.robot.subsystems.shooter.ShooterBase;

public class RunShooterSeriesVelocity extends PIDCommand {

  private SimpleMotorFeedforward m_feedForward;
  private ShooterBase m_shooter;
  private double m_setpoint;
  private static Config m_pidConfig = Config4905.getConfig4905().getPidConstantsConfig();
  private static double m_p = m_pidConfig.getDouble("commands.runshooterseriesvelocity.p");
  private static double m_i = m_pidConfig.getDouble("commands.runshooterseriesvelocity.i");
  private static double m_d = m_pidConfig.getDouble("commands.runshooterseriesvelocity.d");
  private static double m_s = m_pidConfig.getDouble("commands.runshooterseriesvelocity.s");
  private static double m_tolerance = m_pidConfig.getDouble("commands.runshooterseriesvelocity.tolerance");

  public RunShooterSeriesVelocity(ShooterBase shooter, double setpoint) {
    // PID Controller
    super(new PIDController(m_p, m_i, m_d),
        // Measurement
        shooter::getShooterSeriesVelocity,
        // Setpoint
        setpoint,
        // Output
        output -> {
          shooter.setShooterSeriesPower(output);
        });

    getController().setTolerance(m_tolerance);
    m_shooter = shooter;
    m_setpoint = setpoint;
    m_feedForward = new SimpleMotorFeedforward(m_s, 0);
  }

  @Override
  public void execute() {
    getController().calculate(m_shooter.getShooterWheelVelocity(), m_setpoint);
    m_shooter.setPIDIsReady(getController().atSetpoint());
    m_feedForward.calculate(m_setpoint);
  }

}