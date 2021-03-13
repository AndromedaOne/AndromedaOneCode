package frc.robot.commands.pidcommands;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

public class RunShooterWheelVelocity extends PIDCommand4905 {
  private SimpleMotorFeedforward m_feedForward;
  private static double m_computedFeedForward = 0;
  private ShooterBase m_shooter;
  private double m_target = 0;
  private static Config m_pidConfig;
  private double m_pValue = 0;
  private double m_feedForwardValue = 0;
  private boolean m_tuneValues = false;
  private static InterpolatingMap s_kMap = new InterpolatingMap(Config4905.getConfig4905().getCommandConstantsConfig(),
      "shooterTargetRPMAndKValues");
  private static InterpolatingMap s_pMap = new InterpolatingMap(Config4905.getConfig4905().getCommandConstantsConfig(),
      "shooterTargetRPMandPValues");

  /**
   * @param shooter
   * @param m_subsystemController Requires a controller to allow the subsystem
   *                              driver to tune the PID setpoint via the
   *                              controller
   * @param setpoint
   */
  public RunShooterWheelVelocity(ShooterBase shooter, DoubleSupplier setpoint, boolean tuneValues,
      double feedForwardValue, double pValue) {
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
    m_shooter = shooter;
    m_setpoint = setpoint;
    if (tuneValues) {
      m_feedForwardValue = feedForwardValue;
      m_pValue = pValue;
    }
    m_tuneValues = tuneValues;
  }

  public RunShooterWheelVelocity(ShooterBase shooter, DoubleSupplier setpoint) {
    this(shooter, setpoint, false, 0, 0);
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
    m_target = m_setpoint.getAsDouble();
    m_feedForward = createFeedForward();
    double pValue = 0;
    if (m_tuneValues) {
      pValue = m_pValue;
    } else {
      pValue = s_pMap.getInterpolatedValue(m_target);
    }
    getController().setP(pValue);
    getController().setI(m_pidConfig.getDouble("runshooterwheelvelocity.i"));
    getController().setD(m_pidConfig.getDouble("runshooterwheelvelocity.d"));
    System.out.println(" - Shooter Setpoint: " + m_target + "\nShooter P = " + pValue);
  }

  @Override
  public void execute() {

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
    Trace.getInstance().logCommandStop(this);
  }

  private static PIDController4905 createPIDController() {
    m_pidConfig = Config4905.getConfig4905().getCommandConstantsConfig();

    double kp = m_pidConfig.getDouble("runshooterwheelvelocity.p");
    double ki = m_pidConfig.getDouble("runshooterwheelvelocity.i");
    double kd = m_pidConfig.getDouble("runshooterwheelvelocity.d");

    return new PIDController4905("ShooterWheelPID", kp, ki, kd, 0);
  }

  private SimpleMotorFeedforward createFeedForward() {
    double ks = 0;
    double kv = 0;
    if (m_tuneValues) {
      kv = m_feedForwardValue;
    } else {
      kv = s_kMap.getInterpolatedValue(m_target);
    }
    System.out.println("kv " + kv);
    return new SimpleMotorFeedforward(ks, kv);
  }

  public double getSetpoint() {
    return m_target;
  }
}