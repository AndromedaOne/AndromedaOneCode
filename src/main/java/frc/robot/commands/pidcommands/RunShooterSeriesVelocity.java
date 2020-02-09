package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Config4905;
import frc.robot.subsystems.shooter.ShooterBase;

public class RunShooterSeriesVelocity extends PIDCommand {

  private SimpleMotorFeedforward m_feedForward;
  private static double m_computedFeedForward = 0;
  private ShooterBase m_shooter;
  private double m_setpoint = 0;
  private static Config m_pidConfig;

  public RunShooterSeriesVelocity(ShooterBase shooter, double setpoint) {
    // PID Controller
    super(createPIDController(),
        // Measurement
        shooter::getShooterSeriesVelocity,
        // Setpoint
        setpoint,
        // Output
        output -> {
          shooter.setShooterSeriesPower(output + m_computedFeedForward);
        });

    getController().setTolerance(m_pidConfig.getDouble("runshooterseriesvelocity.tolerance"));

    m_feedForward = createFeedForward();

    m_shooter = shooter;
    m_setpoint = setpoint;
  }

  @Override
  public void execute() {
    getController().calculate(m_shooter.getShooterWheelVelocity(), m_setpoint);
    m_shooter.setSeriesPIDIsReady(getController().atSetpoint());
    m_computedFeedForward = m_feedForward.calculate(m_setpoint);
  }

  private static PIDController createPIDController() {
    m_pidConfig = Config4905.getConfig4905().getPidConstantsConfig();

    double kp = m_pidConfig.getDouble("runshooterseriesvelocity.p");
    double ki = m_pidConfig.getDouble("runshooterseriesvelocity.i");
    double kd = m_pidConfig.getDouble("runshooterseriesvelocity.d");

    return new PIDController(kp, ki, kd);
  }

  private SimpleMotorFeedforward createFeedForward() {
    double ks = m_pidConfig.getDouble("runshooterseriesvelocity.s");
    double kv = m_pidConfig.getDouble("runshooterseriesvelocity.v");

    return new SimpleMotorFeedforward(ks, kv);
  }

}