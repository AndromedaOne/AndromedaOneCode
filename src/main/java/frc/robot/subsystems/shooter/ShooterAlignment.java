// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.shooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class ShooterAlignment extends ShooterAlignmentBase {
  SparkMaxController m_angleMotor;
  Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();

  /** Creates a new ShooterAlignment. */
  public ShooterAlignment() {
    m_angleMotor = new SparkMaxController(m_shooterConfig, getShooterName());
  }

  public void rotateShooter(double speed) {
    m_angleMotor.set(speed);
  }

  public boolean atTopLimitSwitch() {
    return m_angleMotor.isForwardLimitSwitchOn();
  }

  public boolean atBottomLimitSwitch() {
    return m_angleMotor.isReverseLimitSwitchOn();
  }

  public void setCoastMode() {
    m_angleMotor.setCoastMode();
  }

  public void setBrakeMode() {
    m_angleMotor.setBrakeMode();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("ShooterAlignmentLimitSwitchTop", atTopLimitSwitch());
    SmartDashboard.putBoolean("ShooterAlignmentLimitSwitchBottom", atBottomLimitSwitch());
    SmartDashboard.putNumber("ShooterAlignmentAngle", getAngle());
  }

  @Override
  public double getAngle() {
    return m_angleMotor.getEncoderPositionTicks() - getOffset();
  }

  @Override
  public String getShooterName() {
    return "angleMotor";
  }

  @Override
  public void stopShooterAlignment() {
    m_angleMotor.set(0);
  }
}