// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.rewrittenWPIclasses;

import edu.wpi.first.math.MathSharedStore;
import edu.wpi.first.math.MathUsageId;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

/**
 * Implements a PID control loop.
 */
public class PIDControllerProposed implements Sendable, AutoCloseable {
  private static int instances;

  // Factor for "proportional" control
  private double m_kp;

  // Factor for "integral" control
  private double m_ki;

  // Factor for "derivative" control
  private double m_kd;

  // The period (in seconds) of the loop that calls the controller
  private final double m_period;

  private double m_maximumIntegral = 1.0;

  private double m_minimumIntegral = -1.0;

  // Maximum input - limit setpoint to this
  private double m_maximumInput;

  // Minimum input - limit setpoint to this
  private double m_minimumInput;

  // Input range - difference between maximum and minimum
  private double m_inputRange;

  // Do the endpoints wrap around? eg. Absolute encoder
  private boolean m_continuous;

  // The error at the time of the most recent call to calculate()
  private double m_positionError;
  private double m_velocityError;

  // The error at the time of the second-most-recent call to calculate() (used to
  // compute velocity)
  private double m_prevError;

  // The sum of the errors for use in the integral calc
  private double m_totalError;

  private double m_pError;
  private double m_iError;
  private double m_dError;

  // The percentage or absolute error that is considered at setpoint.
  private double m_positionTolerance = 0.05;
  private double m_velocityTolerance = Double.POSITIVE_INFINITY;

  private double m_setpoint;
  private double m_measurement;

  /**
   * Allocates a PIDController with the given constants for kp, ki, and kd and a
   * default period of 0.02 seconds.
   *
   * @param kp The proportional coefficient.
   * @param ki The integral coefficient.
   * @param kd The derivative coefficient.
   */
  public PIDControllerProposed(double kp, double ki, double kd) {
    this(kp, ki, kd, 0.02);
  }

  /**
   * Allocates a PIDController with the given constants for kp, ki, and kd.
   *
   * @param kp     The proportional coefficient.
   * @param ki     The integral coefficient.
   * @param kd     The derivative coefficient.
   * @param period The period between controller updates in seconds. Must be
   *               non-zero and positive.
   */
  public PIDControllerProposed(double kp, double ki, double kd, double period) {
    m_kp = kp;
    m_ki = ki;
    m_kd = kd;

    if (period <= 0) {
      throw new IllegalArgumentException("Controller period must be a non-zero positive number!");
    }
    m_period = period;

    instances++;
    SendableRegistry.addLW(this, "PIDControllerProposed", instances);

    MathSharedStore.reportUsage(MathUsageId.kController_PIDController2, instances);
  }

  @Override
  public void close() {
    SendableRegistry.remove(this);
  }

  /**
   * Sets the PID Controller gain parameters.
   *
   * <p>
   * Set the proportional, integral, and differential coefficients.
   *
   * @param kp The proportional coefficient.
   * @param ki The integral coefficient.
   * @param kd The derivative coefficient.
   */
  public void setPID(double kp, double ki, double kd) {
    m_kp = kp;
    m_ki = ki;
    m_kd = kd;
  }

  /**
   * Sets the Proportional coefficient of the PID controller gain.
   *
   * @param kp proportional coefficient
   */
  public void setP(double kp) {
    m_kp = kp;
  }

  /**
   * Sets the Integral coefficient of the PID controller gain.
   *
   * @param ki integral coefficient
   */
  public void setI(double ki) {
    m_ki = ki;
  }

  /**
   * Sets the Differential coefficient of the PID controller gain.
   *
   * @param kd differential coefficient
   */
  public void setD(double kd) {
    m_kd = kd;
  }

  /**
   * Get the Proportional coefficient.
   *
   * @return proportional coefficient
   */
  public double getP() {
    return m_kp;
  }

  /**
   * Get the Integral coefficient.
   *
   * @return integral coefficient
   */
  public double getI() {
    return m_ki;
  }

  /**
   * Get the Differential coefficient.
   *
   * @return differential coefficient
   */
  public double getD() {
    return m_kd;
  }

  /**
   * Returns the period of this controller.
   *
   * @return the period of the controller.
   */
  public double getPeriod() {
    return m_period;
  }

  /**
   * Returns the position tolerance of this controller.
   *
   * @return the position tolerance of the controller.
   */
  public double getPositionTolerance() {
    return m_positionTolerance;
  }

  /**
   * Returns the velocity tolerance of this controller.
   *
   * @return the velocity tolerance of the controller.
   */
  public double getVelocityTolerance() {
    return m_velocityTolerance;
  }

  /**
   * Sets the setpoint for the PIDController.
   *
   * @param setpoint The desired setpoint.
   */
  public void setSetpoint(double setpoint) {
    if (m_maximumInput > m_minimumInput) {
      m_setpoint = MathUtil.clamp(setpoint, m_minimumInput, m_maximumInput);
    } else {
      m_setpoint = setpoint;
    }
    if (m_continuous) {
      double errorBound = (m_maximumInput - m_minimumInput) / 2.0;
      m_positionError = MathUtil.inputModulus(m_setpoint - m_measurement, -errorBound, errorBound);
    } else {
      m_positionError = m_setpoint - m_measurement;
    }

    m_velocityError = (m_positionError - m_prevError) / m_period;
  }

  /**
   * Returns the current setpoint of the PIDController.
   *
   * @return The current setpoint.
   */
  public double getSetpoint() {
    return m_setpoint;
  }

  /**
   * Returns true if the error is within the percentage of the total input range,
   * determined by SetTolerance. This asssumes that the maximum and minimum input
   * were set using SetInput.
   *
   * <p>
   * This will return false until at least one input value has been computed.
   *
   * @return Whether the error is within the acceptable bounds.
   */
  public boolean atSetpoint() {
    return Math.abs(m_positionError) < m_positionTolerance
        && Math.abs(m_velocityError) < m_velocityTolerance;
  }

  /**
   * Enables continuous input.
   *
   * <p>
   * Rather then using the max and min input range as constraints, it considers
   * them to be the same point and automatically calculates the shortest route to
   * the setpoint.
   *
   * @param minimumInput The minimum value expected from the input.
   * @param maximumInput The maximum value expected from the input.
   */
  public void enableContinuousInput(double minimumInput, double maximumInput) {
    m_continuous = true;
    setInputRange(minimumInput, maximumInput);
  }

  /** Disables continuous input. */
  public void disableContinuousInput() {
    m_continuous = false;
  }

  /**
   * Returns true if continuous input is enabled.
   *
   * @return True if continuous input is enabled.
   */
  public boolean isContinuousInputEnabled() {
    return m_continuous;
  }

  /**
   * Sets the minimum and maximum values for the integrator.
   *
   * <p>
   * When the cap is reached, the integrator value is added to the controller
   * output rather than the integrator value times the integral gain.
   *
   * @param minimumIntegral The minimum value of the integrator.
   * @param maximumIntegral The maximum value of the integrator.
   */
  public void setIntegratorRange(double minimumIntegral, double maximumIntegral) {
    m_minimumIntegral = minimumIntegral;
    m_maximumIntegral = maximumIntegral;
  }

  /**
   * Sets the error which is considered tolerable for use with atSetpoint().
   *
   * @param positionTolerance Position error which is tolerable.
   */
  public void setTolerance(double positionTolerance) {
    setTolerance(positionTolerance, Double.POSITIVE_INFINITY);
  }

  /**
   * Sets the error which is considered tolerable for use with atSetpoint().
   *
   * @param positionTolerance Position error which is tolerable.
   * @param velocityTolerance Velocity error which is tolerable.
   */
  public void setTolerance(double positionTolerance, double velocityTolerance) {
    m_positionTolerance = positionTolerance;
    m_velocityTolerance = velocityTolerance;
  }

  /**
   * Returns the difference between the setpoint and the measurement.
   *
   * @return The error.
   */
  public double getContinuousPositionError() {
    return getContinuousError(m_positionError);
  }

  public double getPositionError() {
    return m_positionError;
  }

  /**
   * Returns the velocity error.
   *
   * @return The velocity error.
   */
  public double getVelocityError() {
    return m_velocityError;
  }

  /**
   * Returns the next output of the PID controller.
   *
   * @param measurement The current measurement of the process variable.
   * @param setpoint    The new setpoint of the controller.
   * @return The next controller output.
   */
  public double calculate(double measurement, double setpoint) {
    // Set setpoint to provided value
    setSetpoint(setpoint);
    return calculate(measurement);
  }

  /**
   * Returns the next output of the PID controller.
   *
   * @param measurement The current measurement of the process variable.
   * @return The next controller output.
   */
  public double calculate(double measurement) {
    m_measurement = measurement;
    m_prevError = m_positionError;
    m_positionError = getContinuousError(m_setpoint - measurement);
    m_velocityError = (m_positionError - m_prevError) / m_period;

    if (m_ki != 0) {
      m_totalError = MathUtil.clamp(m_totalError + m_positionError * m_period,
          m_minimumIntegral / m_ki, m_maximumIntegral / m_ki);
    }

    m_pError = m_kp * m_positionError;
    m_iError = m_ki * m_totalError;
    m_dError = m_kd * m_velocityError;

    return m_pError + m_iError + m_dError;
  }

  protected double getPError() {
    return m_pError;
  }

  protected double getIError() {
    return m_iError;
  }

  protected double getDError() {
    return m_dError;
  }

  /** Resets the previous error and the integral term. */
  public void reset() {
    m_positionError = 0;
    m_prevError = 0;
    m_totalError = 0;
    m_velocityError = 0;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.setSmartDashboardType("PIDController");
    builder.addDoubleProperty("p", this::getP, this::setP);
    builder.addDoubleProperty("i", this::getI, this::setI);
    builder.addDoubleProperty("d", this::getD, this::setD);
    builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
  }

  /**
   * Wraps error around for continuous inputs. The original error is returned if
   * continuous mode is disabled.
   *
   * @param error The current error of the PID controller.
   * @return Error for continuous inputs.
   */
  protected double getContinuousError(double error) {
    if (m_continuous && m_inputRange > 0) {
      error %= m_inputRange;
      if (Math.abs(error) > m_inputRange / 2) {
        if (error > 0) {
          return error - m_inputRange;
        } else {
          return error + m_inputRange;
        }
      }
    }
    return error;
  }

  /**
   * Sets the minimum and maximum values expected from the input.
   *
   * @param minimumInput The minimum value expected from the input.
   * @param maximumInput The maximum value expected from the input.
   */
  private void setInputRange(double minimumInput, double maximumInput) {
    m_minimumInput = minimumInput;
    m_maximumInput = maximumInput;
    m_inputRange = maximumInput - minimumInput;

    // Clamp setpoint to new input
    if (m_maximumInput > m_minimumInput) {
      m_setpoint = MathUtil.clamp(m_setpoint, m_minimumInput, m_maximumInput);
    }
  }
}
