// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunShooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class TopShooterWheel extends SubsystemBase implements ShooterWheelBase {

  private Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
  private SparkMaxController m_shooterMotor;

  public TopShooterWheel() {
    m_shooterMotor = new SparkMaxController(m_shooterConfig, getShooterName());
  }

  @Override
  public void setShooterWheelPower(double power) {
    m_shooterMotor.setSpeed(power);
    SmartDashboard.putNumber("TopShooterWheelPower", power);

  }

  @Override
  public double getShooterWheelPower() {
    return m_shooterMotor.getSpeed();
  }

  @Override
  public double getShooterWheelRpm() {
    double speed = m_shooterMotor.getEncoderVelocityTicks();
    SmartDashboard.putNumber("TopShooterSpeed", speed);
    return speed;
  }

  @Override
  public String getShooterName() {
    return "topShooterWheel";
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
    super.setDefaultCommand(command);
  }
}
