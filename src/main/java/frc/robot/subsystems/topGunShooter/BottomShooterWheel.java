// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunShooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class BottomShooterWheel extends SubsystemBase implements ShooterWheelBase {

  private Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
  private SparkMaxController m_shooterMotor;

  public BottomShooterWheel() {
    m_shooterMotor = new SparkMaxController(m_shooterConfig, getShooterName());
  }

  @Override
  public void setShooterWheelPower(double power) {
    m_shooterMotor.setSpeed(power);
    SmartDashboard.putNumber("BottomShooterWheelPower", power);

  }

  @Override
  public double getShooterWheelPower() {
    return m_shooterMotor.getSpeed();
  }

  @Override
  public double getShooterWheelRpm() {
    double speed = m_shooterMotor.getEncoderVelocityTicks();
    SmartDashboard.putNumber("BottomShooterSpeed", speed);
    return speed;
  }

  @Override
  public String getShooterName() {
    return "bottomShooterWheel";
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
