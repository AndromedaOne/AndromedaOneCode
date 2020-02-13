package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class RunShooterWheelVelocity extends PIDCommand {
  private SimpleMotorFeedforward m_feedForward;
  private static double m_computedFeedForward = 0;
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
  public RunShooterWheelVelocity(ShooterBase shooter, double setpoint) {
    // PID Controller
    super(createPIDController(),
        // Measurement
        shooter::getShooterWheelVelocity,
        // Setpoint
        setpoint,
        // Output
        output -> {
          shooter.setShooterWheelPower(output + m_computedFeedForward);
        });

    getController().setTolerance(m_pidConfig.getDouble("runshooterwheelvelocity.tolerance"));

    m_feedForward = createFeedForward();

    m_shooterConfig = Config4905.getConfig4905().getShooterConfig();

    kControllerScale = m_shooterConfig.getDouble("shooterwheeljoystickscale");
    m_shooter = shooter;
    m_setpoint = setpoint;
    Trace.getInstance().logCommandStart("RunShooterWheelVelocity");
  }

  @Override
  public void execute() {
    double leftYAxis = Robot.getInstance().getOIContainer().getSubsystemController().getLeftStickForwardBackwardValue();
    // This adjusts the setpoint while the PID is running to allow the
    // Subsystems driver to tune the rpm on the fly
    m_setpoint += leftYAxis * kControllerScale;

    getController().calculate(m_shooter.getShooterWheelVelocity(), m_setpoint);
    m_shooter.setShooterPIDIsReady(getController().atSetpoint());
    m_computedFeedForward = m_feedForward.calculate(m_setpoint);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  private static PIDController createPIDController() {
    m_pidConfig = Config4905.getConfig4905().getPidConstantsConfig();

    double kp = m_pidConfig.getDouble("runshooterwheelvelocity.p");
    double ki = m_pidConfig.getDouble("runshooterwheelvelocity.i");
    double kd = m_pidConfig.getDouble("runshooterwheelvelocity.d");

    return new PIDController(kp, ki, kd);
  }

  private SimpleMotorFeedforward createFeedForward() {
    double ks = m_pidConfig.getDouble("runshooterwheelvelocity.s");
    double kv = m_pidConfig.getDouble("runshooterwheelvelocity.v");

    return new SimpleMotorFeedforward(ks, kv);
  }
}