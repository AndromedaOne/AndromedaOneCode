// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.pidcontroller.PIDCommand4905;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;
import frc.robot.utils.InterpolatingMap;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RunOneShooterWheelVelocity extends PIDCommand4905 {
  /** Creates a new RunOneShooterWheelVelocity. */
  private ShooterBase m_shooterWheel;
  private DoubleSupplier m_setpoint;
  private boolean m_tuneValues;
  private double m_feedForwardValue;
  private double m_pValue;
  private static double m_computedFeedForward = 0;
  private double m_target = 0;
  private static Config m_shooterConfig;
  private SimpleMotorFeedforward m_feedForward;
  private InterpolatingMap m_kMap;
  private InterpolatingMap m_pMap;
  private BooleanSupplier m_finishedCondition;

  public RunOneShooterWheelVelocity(ShooterBase shooterWheel, DoubleSupplier setpoint,
      boolean tuneValues, double feedForwardValue, double pValue, Config shooterConfig,
      BooleanSupplier finishedCondition) {
    super(
        // The controller that the command will use
        new PIDController4905SampleStop(shooterWheel.getShooterName(), 0, 0, 0, 0),
        // This should return the measurement
        // () -> 0,
        shooterWheel::getShooterWheelRpm,
        // This should return the setpoint (can also be a constant)
        // () -> 0,
        setpoint,
        // This uses the output
        output -> {
          // Use the output here
          shooterWheel.setShooterWheelPower(output + m_computedFeedForward);
        });
    addRequirements(shooterWheel);
    // Configure additional PID options by calling `getController` here.
    m_shooterConfig = shooterConfig;
    getController()
        .setTolerance(m_shooterConfig.getDouble(shooterWheel.getShooterName() + ".tolerance"));
    m_shooterWheel = shooterWheel;
    m_setpoint = setpoint;
    if (tuneValues) {
      m_feedForwardValue = feedForwardValue;
      m_pValue = pValue;
    }
    m_tuneValues = tuneValues;
    m_kMap = new InterpolatingMap(shooterConfig,
        shooterWheel.getShooterName() + ".shooterTargetRPMAndKValues");
    m_pMap = new InterpolatingMap(shooterConfig,
        shooterWheel.getShooterName() + ".shooterTargetRPMandPValues");
    m_finishedCondition = finishedCondition;
  }

  public RunOneShooterWheelVelocity(ShooterBase shooterWheel, DoubleSupplier setpoint,
      Config shooterConfig, BooleanSupplier finishedCondition) {
    this(shooterWheel, setpoint, false, 0, 0, shooterConfig, finishedCondition);
  }

  // Returns true when the command should end.
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
      pValue = m_pMap.getInterpolatedValue(m_target);
    }
    getController().setP(pValue);
    getController().setI(
        m_shooterConfig.getDouble(m_shooterWheel.getShooterName() + ".runshooterwheelvelocity.i"));
    getController().setD(
        m_shooterConfig.getDouble(m_shooterWheel.getShooterName() + ".runshooterwheelvelocity.d"));
    System.out.println(m_shooterWheel.getShooterName() + "Setpoint: " + m_target + "\n"
        + m_shooterWheel.getShooterName() + " P = " + pValue);
  }

  @Override
  public void execute() {
    m_computedFeedForward = m_feedForward.calculate(m_target);
    super.execute();
    SmartDashboard.putNumber(m_shooterWheel.getShooterName() + " Wheel Velocity Setpoint",
        m_target);
    // Need to add a reference for top or bottom wheel to include in the print-out
  }

  @Override
  public boolean isFinished() {
    return m_finishedCondition.getAsBoolean();
  }

  @Override
  public void end(boolean interrupt) {
    m_shooterWheel.setShooterWheelPower(0);
    Trace.getInstance().logCommandStop(this);
  }

  private SimpleMotorFeedforward createFeedForward() {
    double ks = 0;
    double kv = 0;
    if (m_tuneValues) {
      kv = m_feedForwardValue;
    } else {
      kv = m_kMap.getInterpolatedValue(m_target);
    }
    System.out.println("kv " + kv);
    return new SimpleMotorFeedforward(ks, kv);
  }

  public double getSetpoint() {
    return m_target;
  }
}
