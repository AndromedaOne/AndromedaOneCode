// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.pidcontroller;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

import java.util.Set;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * A command that controls an output with a {@link PIDController}. Runs forever
 * by default - to add exit conditions and/or other behavior, subclass this
 * class. The controller calculation and output are performed synchronously in
 * the command's execute() method.
 *
 * <p>
 * This class is provided by the NewCommands VendorDep
 */
public class PIDCommand4905 extends Command {
  private final PIDController4905 m_controller;
  private DoubleSupplier m_measurement;
  private DoubleSupplier m_setpoint;
  private DoubleConsumer m_useOutput;

  /**
   * Creates a new PIDCommand, which controls the given output with a
   * PIDController.
   *
   * @param controller        the controller that controls the output.
   * @param measurementSource the measurement of the process variable
   * @param setpointSource    the controller's setpoint
   * @param useOutput         the controller's output
   * @param feedForward       feedforward that implements calculate
   * @param requirements      the subsystems required by this command
   */
  public PIDCommand4905(PIDController4905 controller, DoubleSupplier measurementSource,
      DoubleSupplier setpointSource, DoubleConsumer useOutput, FeedForward feedForward,
      Subsystem... requirements) {
    requireNonNullParam(controller, "controller", "PIDCommand");
    requireNonNullParam(measurementSource, "measurementSource", "PIDCommand");
    requireNonNullParam(setpointSource, "setpointSource", "PIDCommand");
    requireNonNullParam(useOutput, "useOutput", "PIDCommand");
    requireNonNullParam(feedForward, "feedforward", "PIDCommand");

    m_controller = controller;
    m_controller.setFeedforward(feedForward);
    m_useOutput = useOutput;
    m_measurement = measurementSource;
    m_setpoint = setpointSource;
    m_requirements.addAll(Set.of(requirements));
  }

  /**
   * Creates a new PIDCommand, which controls the given output with a
   * PIDController that does not use feedforwared
   *
   * @param controller        the controller that controls the output.
   * @param measurementSource the measurement of the process variable
   * @param setpoint          the controller's setpoint
   * @param useOutput         the controller's output
   * @param requirements      the subsystems required by this command
   */
  public PIDCommand4905(PIDController4905 controller, DoubleSupplier measurementSource,
      DoubleSupplier setpoint, DoubleConsumer useOutput, Subsystem... requirements) {
    this(controller, measurementSource, setpoint, useOutput, () -> 0, requirements);
  }

  @Override
  public void initialize() {
    m_controller.reset();
  }

  @Override
  public void execute() {
    m_useOutput
        .accept(m_controller.calculate(m_measurement.getAsDouble(), m_setpoint.getAsDouble()));
  }

  @Override
  public void end(boolean interrupted) {
    m_useOutput.accept(0);
  }

  /**
   * Returns the PIDController used by the command.
   *
   * @return The PIDController
   */
  public PIDController4905 getController() {
    return m_controller;
  }

  public void setOutput(DoubleConsumer output) {
    m_useOutput = output;
  }

  public void setSetpoint(DoubleSupplier setpoint) {
    m_setpoint = setpoint;
  }

  public DoubleSupplier getSetpoint() {
    return m_setpoint;
  }

  public void setUseOutput(DoubleConsumer output) {
    m_useOutput = output;
  }

  public void setMeasurementSource(DoubleSupplier source) {
    m_measurement = source;
  }
}
