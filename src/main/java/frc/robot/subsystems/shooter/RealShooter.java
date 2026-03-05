// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealShooter extends SubsystemBase implements ShooterBase {

  private SparkMaxController m_leaderMotor;
  // the follower motor should mirror the output of the leader
  private SparkMaxController m_followerMotor;
  private boolean m_isAtSetpoint = true;
  private Config m_config = Config4905.getConfig4905().getShooterConfig();

  public RealShooter() {
    m_leaderMotor = new SparkMaxController(m_config, "leaderMotor", false, false);
    m_followerMotor = new SparkMaxController(m_config, "followerMotor", false, false);
  }

  /**
   * In RPM
   */
  @Override
  public double getShooterVelocity() {
    return -m_leaderMotor.getBuiltInEncoderVelocityTicks();
  }

  /**
   * Run this in the command
   */
  @Override
  public void runShooter(double speed) {
    m_leaderMotor.setSpeed(-speed);
  }

  @Override
  public void stop() {
    m_leaderMotor.setSpeed(0);
    m_followerMotor.setSpeed(0);
  }

  @Override
  public void setBrakeMode() {
    m_leaderMotor.setBrakeMode();
    m_followerMotor.setBrakeMode();
  }

  @Override
  public void setCoastMode() {
    m_leaderMotor.setCoastMode();
    m_followerMotor.setCoastMode();
  }

  @Override
  public void setSetpointStatus(boolean isAtSetpoint) {
    m_isAtSetpoint = isAtSetpoint;
  }

  @Override
  public boolean isAtRPMSetpoint() {
    return m_isAtSetpoint;
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber(s_shooterString + "Shooter Speed", getShooterVelocity());
  }

}
