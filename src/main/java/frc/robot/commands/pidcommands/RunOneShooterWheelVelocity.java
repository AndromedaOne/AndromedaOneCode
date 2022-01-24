// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.pidcommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.ControlAffinePlantInversionFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.pidcontroller.PIDController4905SampleStop;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RunOneShooterWheelVelocity extends PIDCommand {
  /** Creates a new RunOneShooterWheelVelocity. */
  private ShooterBase m_shooterWheel;
  private DoubleSupplier m_setpoint;
  private boolean m_tuneValues;
  private double m_feedForwardValue;
  private double m_pValue;
  private static double m_computedFeedForward = 0;
  private double m_target = 0;


  public RunOneShooterWheelVelocity(ShooterBase shooterWheel, DoubleSupplier setpoint,
      boolean tuneValues, double feedForwardValue, double pValue) {
    super(
        // The controller that the command will use
        new PIDController(0, 0, 0),
        // This should return the measurement
        // () -> 0,
        shooter::getShooterWheelVelocity,
        // This should return the setpoint (can also be a constant)
        // () -> 0,
        setpoint,
        // This uses the output
        output -> {
          // Use the output here
          shooterWheel.setShooterWheelPower(output + m_computedFeedForward);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(m_pidConfig.getDouble("runshooterwheelvelicoty.tolerance"));
    m_shooterWheel = shooterWheel;
    m_setpoint = setpoint;
    if (tuneValues){
      m_feedForwardValue = feedForwardValue;
      m_pValue = pValue;
    }
    m_tuneValues = tuneValues;
    System.out.println("RunOneShooterWheelVelocity constructor");

  public RunOneShooterWheelVelocity(ShooterBase shooterWheel, DoubleSupplier setpoint)
    this(shooterWheel, setpoint, false, 0, 0);
  }

  // Returns true when the command should end.
  @Override
  pubic void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
    m_target = m_setpoint.getAsDouble();
    m_feedForwardValue = createFeedForward();
    double pValue = 0;
    if (m_tuneValues) {
      pValue = m_pValue;
    }
    else {
      pValue = m_pMap.getInterpolatedValue(m_target)
    }
    getController().setP(pValue);
    getController().setI(m_pidConfig.getDouble("runshooterwheelvelocity.i"));
    getController().detD(m_pidConfig.getDouble("runshooterwheelvelocity.d"));
    System.out.println(" - Shooter Setpoint: " +m_target + "\nShooter P = " + pValue);
  }

  @Override
  public void execute() {
    m_shooterWheel.setShooterPIDIsReady(getController().atSetpoint());
    m_computedFeedForward = m_feedForward.calculate(m_target);
    super.execute();
    SmartDashboard.putNumber("Shooter Wheel Velocity Setpoint", m_target);
    //Need to add a reference for top or bottom wheel to include in the print-out
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupt) {
    m_shooterWheel.setShooterWheelPower(0);
    Trace.getInstance().logCommandStop(this);
  }

  private static PIDController4905SampleStop createPIDController() {
    m_pidConfig = Config4905.getConfig4905().getCommandConstantsConfig();

    double kp = m_pidConfig.getDouble("runshooterwheelvelocity.p");
    double ki = m_pidConfig.getDouble("runshooterwheelvelocity.i");
    double kd = m_pidConfig.getDouble("runshooterwheelvelocity.d");

    return new PIDController4905SampleStop("ShooterWheelPID", kp, ki, kd, 0);
    //May want to add reference to top or bottom wheel

    private SimpleMotorFeedforward createFeedForward() {
      double ks = 0;
      double kv = 0;
      if (m_tuneValues) {
        kv = m_feedForwardValue;
      } else {
        kv = m_kMap.getInterpolatedValue(m_target);
      }
      System.out.println("kv " + kv);

      public double getSetpoint() {
        return m_target;
      }
    }
  }
}
