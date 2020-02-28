package frc.robot.commands.pidcommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.shooter.ShooterBase;

public class RunShooterWheelVelocity extends PIDCommand4905 {
  private SimpleMotorFeedforward m_feedForward;
  private static double m_computedFeedForward = 0;
  private ShooterBase m_shooter;
  private double m_target = 0;
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
        0,
        // Output
        output -> {
          shooter.setShooterWheelPower(output + m_computedFeedForward);
        });

    getController().setTolerance(m_pidConfig.getDouble("runshooterwheelvelocity.tolerance"));

    m_shooterConfig = Config4905.getConfig4905().getShooterConfig();

    kControllerScale = m_shooterConfig.getDouble("shooterwheeljoystickscale");
    m_shooter = shooter;
    m_target = setpoint;
    m_setpoint = this::getSetpoint;
  }

  @Override
  public void initialize() {
    super.initialize();
    m_feedForward = createFeedForward();
    getController().setP(m_pidConfig.getDouble("runshooterwheelvelocity.p"));
    getController().setI(m_pidConfig.getDouble("runshooterwheelvelocity.i"));
    getController().setD(m_pidConfig.getDouble("runshooterwheelvelocity.d"));
    System.out.println(
        " - Shooter Setpoint: " + m_target + "\nShooter P = " + m_pidConfig.getDouble("runshooterwheelvelocity.p"));
    }

  @Override
  public void execute() {
    double leftYAxis = Robot.getInstance().getOIContainer().getSubsystemController().getLeftStickForwardBackwardValue();
    // This adjusts the setpoint while the PID is running to allow the
    // Subsystems driver to tune the rpm on the fly
    if (Math.abs(leftYAxis) < .1) {
      leftYAxis = 0;
    }
    m_target += leftYAxis * kControllerScale;
    if (m_target > 4900) {
      m_target = 4900;
    }
    m_shooter.setShooterPIDIsReady(getController().atSetpoint());
    m_computedFeedForward = m_feedForward.calculate(m_target);
    super.execute();
    SmartDashboard.putNumber("Shooter Wheel Velocity Setpoint", m_target);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupt) {
    m_shooter.setShooterWheelPower(0);
  }

  private static PIDController4905 createPIDController() {
    m_pidConfig = Config4905.getConfig4905().getPidConstantsConfig();

    double kp = m_pidConfig.getDouble("runshooterwheelvelocity.p");
    double ki = m_pidConfig.getDouble("runshooterwheelvelocity.i");
    double kd = m_pidConfig.getDouble("runshooterwheelvelocity.d");

    return new PIDController4905("ShooterWheelPID", kp, ki, kd, 0);
  }

  private SimpleMotorFeedforward createFeedForward() {
    double ks = m_pidConfig.getDouble("runshooterwheelvelocity.s");
    double kv = m_pidConfig.getDouble("runshooterwheelvelocity.v");

    return new SimpleMotorFeedforward(ks, kv);
  }

  public double getSetpoint() {
    return m_target;
  }
}