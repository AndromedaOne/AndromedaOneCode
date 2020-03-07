package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.shooter.ShooterBase;

public class RunShooterSeriesVelocity extends PIDCommand4905 {

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
    super(createPIDController(),
        // Measurement
        shooter::getShooterSeriesVelocity,
        // Setpoint
        setpoint,
        // Output
        output -> {
          shooter.setShooterSeriesPower(1);
        });

    getController().setTolerance(m_pidConfig.getDouble("runshooterseriesvelocity.tolerance"));

    kControllerScale = Config4905.getConfig4905().getCommandConstantsConfig()
        .getDouble("RunShooterSeriesVelocity.shooterwheeljoystickscale");
    m_shooter = shooter;
    m_setpoint = setpoint;
  }

  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public void execute() {
    double leftYAxis = Robot.getInstance().getOIContainer().getSubsystemController().getLeftStickForwardBackwardValue();
    // This adjusts the setpoint while the PID is running to allow the
    // Subsystems driver to tune the rpm on the fly
    m_setpoint += leftYAxis * kControllerScale;
    m_shooter.setSeriesPIDIsReady(getController().atSetpoint());
    super.execute();
  }

  @Override
  public void end(boolean interrupt) {
    m_shooter.setShooterSeriesPower(0);
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