package frc.robot.commands.billthovenShooterCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.pidcontroller.FeedForward;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.billShooter.BillShooterBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RunBillShooterWheelVelocity extends PIDCommand4905 {
  /** Creates a new RunOneShooterWheelVelocity. */
  private BillShooterBase m_shooterWheel;
  private DoubleSupplier m_setpoint;
  private boolean m_tuneValues;
  private double m_feedForwardValue;
  private double m_pValue;
  private double m_target = 0;
  private static Config m_shooterConfig;
  private FeedForward m_feedForward = new ShooterFeedForward();
  private InterpolatingMap m_kMap;
  private BooleanSupplier m_finishedCondition;

  private class ShooterFeedForward implements FeedForward {
    @Override
    public double calculate() {
      double kv = 0;
      if (m_tuneValues) {
        kv = m_feedForwardValue;
      } else {
        kv = m_kMap.getInterpolatedValue(m_target);
        SmartDashboard.putString("ShooterCalc Use Interp", " " + kv);
      }
      SmartDashboard.putNumber("Shooter Feed Forward ", kv);
      SmartDashboard.putNumber("ShooterCalc m_target", m_target);
      return kv;
    }
  }

  public RunBillShooterWheelVelocity(BillShooterBase shooterWheel, DoubleSupplier setpoint,
      boolean tuneValues, double feedForwardValue, double pValue, Config shooterConfig,
      BooleanSupplier finishedCondition) {
    super(
        // The controller that the command will use
        new PIDController4905SampleStop(shooterWheel.toString()),
        // This should return the measurement
        // () -> 0,
        shooterWheel::getShooterWheelRpm,
        // This should return the setpoint (can also be a constant)
        // () -> 0,
        setpoint,
        // This uses the output
        output -> {
          // Use the output here
          shooterWheel.setShooterWheelPower(output);
        });
    addRequirements(shooterWheel.getSubsystemBase());
    // Configure additional PID options by calling `getController` here.
    m_shooterConfig = shooterConfig;
    getController().setTolerance(m_shooterConfig.getDouble("shooterMotor.tolerance"));
    getController().setFeedforward(m_feedForward);
    m_shooterWheel = shooterWheel;
    m_setpoint = setpoint;
    if (tuneValues) {
      m_feedForwardValue = feedForwardValue;
      m_pValue = pValue;
    }
    m_tuneValues = tuneValues;
    m_kMap = new InterpolatingMap(shooterConfig, "shooterMotor.shooterTargetRPMAndKValues");
    m_finishedCondition = finishedCondition;
  }

  public RunBillShooterWheelVelocity(BillShooterBase shooterWheel, DoubleSupplier setpoint,
      Config shooterConfig, BooleanSupplier finishedCondition) {
    this(shooterWheel, setpoint, false, 0, 0, shooterConfig, finishedCondition);
  }

  // Returns true when the command should end.
  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart(this);
    double pValue = 0;
    if (m_tuneValues) {
      pValue = m_pValue;
    } else {
      pValue = m_shooterConfig.getDouble("shooterMotor.runshooterwheelvelocity.p");
    }
    getController().setP(pValue);
    getController().setI(m_shooterConfig.getDouble("shooterMotor.runshooterwheelvelocity.i"));
    getController().setD(m_shooterConfig.getDouble("shooterMotor.runshooterwheelvelocity.d"));
    System.out.println("Shooter Setpoint: " + m_target + "  P = " + pValue);
  }

  @Override
  public void execute() {
    m_target = m_setpoint.getAsDouble();
    super.execute();
    SmartDashboard.putNumber(m_shooterWheel.toString() + " Wheel Velocity Setpoint", m_target);
  }

  @Override
  public boolean isFinished() {
    return m_finishedCondition.getAsBoolean();
  }

  @Override
  public void end(boolean interrupt) {
    m_shooterWheel.setShooterWheelPower(0);
  }

  public boolean atSetpoint() {
    return getController().atSetpoint();
  }
}
