// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;

/** Add your docs here. */
public class RealShooter extends SubsystemBase implements ShooterBase {

  private SparkMaxController m_motor;
  private PIDController4905 m_controller;
  private double m_kp;
  private double m_ki;
  private double m_kd;
  private Config m_config = Config4905.getConfig4905().getShooterConfig();

  public RealShooter() {
    m_motor = new SparkMaxController(m_config, "shooterMotor", false, false);
    m_controller = new PIDController4905("shooterPID");
    m_kp = m_config.getDouble("kp");
    m_ki = m_config.getDouble("ki");
    m_kd = m_config.getDouble("kd");
    m_controller.setPID(m_kp, m_ki, m_kd);
  }

  @Override
  public void setVelocitySetpoint(double RPM) {
    m_controller.setSetpoint(RPM);
  }

  /**
   * In RPM
   */
  @Override
  public double getShooterVelocity() {
    return m_motor.getBuiltInEncoderVelocityTicks();
  }

  /**
   * Run this in the command
   */
  @Override
  public void runShooter() {
    double pidCalc = m_controller.calculate(getShooterVelocity());
    m_motor.setSpeed(pidCalc);
  }

  @Override
  public boolean isAtSetpoint() {
    return m_controller.atSetpoint();
  }

  @Override
  public void stop() {
    m_motor.setSpeed(0);
  }

  @Override
  public void setPID(double kp, double ki, double kd) {
    m_kp = kp;
    m_ki = ki;
    m_kd = kd;
    m_controller.setPID(m_kp, m_ki, m_kd);
  }

  @Override
  public void setBrakeMode() {
    m_motor.setBrakeMode();
  }

  @Override
  public void setCoastMode() {
    m_motor.setCoastMode();
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

}
