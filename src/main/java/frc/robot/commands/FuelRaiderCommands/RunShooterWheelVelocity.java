package frc.robot.commands.FuelRaiderCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RunShooterWheelVelocity extends PIDCommand4905 {
  /** Creates a new RunOneShooterWheelVelocity. */
  private ShooterBase m_shooter;
  private DoubleSupplier m_setpoint;
  private boolean m_tuneValues;
  private double m_feedForwardValue;
  private double m_pValue;
  private double m_target = 0;
  private static Config m_shooterConfig;
  private FeedForward m_feedForward = new ShooterFeedForward();
  private InterpolatingMap m_kMap;
  private BooleanSupplier m_finishedCondition;
  private String m_smartDashboardName = ShooterBase.getSmartDashboardShooterString();

  private class ShooterFeedForward implements FeedForward {
    @Override
    public double calculate() {
      double kv = 0;
      if (m_tuneValues) {
        kv = m_feedForwardValue;
      } else {
        kv = m_kMap.getInterpolatedValue(m_target);
        SmartDashboard.putString(m_smartDashboardName + "Use Interp", " " + kv);
      }
      SmartDashboard.putNumber(m_smartDashboardName + "Feed Forward ", kv);
      return kv;
    }
  }

  public RunShooterWheelVelocity(ShooterBase shooter, DoubleSupplier setpoint, boolean tuneValues,
      double feedForwardValue, double pValue, Config shooterConfig,
      BooleanSupplier finishedCondition) {
    super(
        // The controller that the command will use
        new PIDController4905SampleStop("ShooterPID"),
        // This should return the measurement
        // () -> 0,
        () -> shooter.getShooterVelocity(),
        // This should return the setpoint (can also be a constant)
        // () -> 0,
        setpoint,
        // This uses the output
        output -> {
          // Use the output here
          shooter.runShooter(output);
        });
    addRequirements(shooter.getSubsystemBase());
    // Configure additional PID options by calling `getController` here.
    m_shooterConfig = shooterConfig;
    getController().setTolerance(m_shooterConfig.getDouble("tolerance"));
    getController().setFeedforward(m_feedForward);
    m_shooter = shooter;
    m_setpoint = setpoint;
    if (tuneValues) {
      m_feedForwardValue = feedForwardValue;
      m_pValue = pValue;
    }
    m_tuneValues = tuneValues;
    m_kMap = new InterpolatingMap(shooterConfig, "shooterTargetRPMAndKValues");
    m_finishedCondition = finishedCondition;
    Trace.getInstance().logInfo("Setpoint " + m_setpoint.getAsDouble());
  }

  public RunShooterWheelVelocity(ShooterBase shooter, DoubleSupplier setpoint, Config shooterConfig,
      BooleanSupplier finishedCondition) {
    this(shooter, setpoint, false, 0, 0, shooterConfig, finishedCondition);
  }

  // Returns true when the command should end.
  @Override
  public void initialize() {
    super.initialize();
    double pValue = 0;
    if (m_tuneValues) {
      pValue = m_pValue;
    } else {
      // replace with pmap at some point
      pValue = m_shooterConfig.getDouble("runshooterwheelvelocity.p");
    }
    m_shooter.setSetpointStatus(false);
    getController().setP(pValue);
    getController().setI(m_shooterConfig.getDouble("runshooterwheelvelocity.i"));
    getController().setD(m_shooterConfig.getDouble("runshooterwheelvelocity.d"));
    Trace.getInstance().logCommandInfo(this, "  P = " + pValue);
    Trace.getInstance().logCommandInfo(this, "setpoint: " + getController().getSetpoint());
  }

  @Override
  public void execute() {
    m_target = m_setpoint.getAsDouble();
    super.execute();
    SmartDashboard.putNumber(m_smartDashboardName + "Shooter Wheel Velocity Setpoint", m_target);
    SmartDashboard.putNumber(m_smartDashboardName + "Shooter angular velocity",
        m_shooter.getShooterVelocity());
    SmartDashboard.putNumber(m_smartDashboardName + "Shooter PID controller setpoint",
        getController().getSetpoint());
    m_shooter.setSetpointStatus(atSetpoint());
  }

  @Override
  public boolean isFinished() {
    return m_finishedCondition.getAsBoolean();
  }

  @Override
  public void end(boolean interrupt) {
    m_shooter.runShooter(0);
    m_shooter.setSetpointStatus(true);
  }

  public boolean atSetpoint() {
    return getController().atSetpoint();
  }
}
