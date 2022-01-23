// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealShooter extends ShooterBase {

  private Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
  private SparkMaxController m_shooterMotor1;
  private SparkMaxController m_shooterMotor2;
  private MotorControllerGroup m_motorGroup;

  public RealShooter() {
    m_shooterMotor1 = new SparkMaxController(m_shooterConfig, "shooter1");
    m_shooterMotor2 = new SparkMaxController(m_shooterConfig, "shooter2");
    m_motorGroup = new MotorControllerGroup(m_shooterMotor1, m_shooterMotor2);

  }

  @Override
  public void setShooterWheelPower(double power) {
    m_motorGroup.set(power);
    SmartDashboard.putNumber("ShooterWheelPower", power);

  }

  @Override
  public double getShooterWheelPower() {
    return m_motorGroup.get();
  }

  @Override
  public double getShooterWheelRpm() {
    double averageSpeed = (m_shooterMotor1.getEncoderVelocityTicks()
        + m_shooterMotor2.getEncoderVelocityTicks()) / 2;
    SmartDashboard.putNumber("AverageShooterSpeed", averageSpeed);
    return averageSpeed;
  }
}
