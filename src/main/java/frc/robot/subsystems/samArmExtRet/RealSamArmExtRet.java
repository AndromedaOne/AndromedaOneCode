// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import com.revrobotics.SparkMaxLimitSwitch;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealSamArmExtRet extends SamArmExtRetBase {

  private final SparkMaxController m_extensionMotor;
  private double m_zeroOffset = 0;
  private double m_maxExtension = 0;
  private double m_minExtension = 0;
  private boolean m_isInitialized = false;

  /** Creates a new RealSamArmExtension. */
  public RealSamArmExtRet() {
    Config armextensionConfig = Config4905.getConfig4905().getSamArmExtensionConfig();

    m_extensionMotor = new SparkMaxController(armextensionConfig, "extensionmotor");
    m_maxExtension = armextensionConfig.getDouble("maxExtension");
    m_minExtension = armextensionConfig.getDouble("minExtension");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("arm extension position", getPosition());
    SmartDashboard.putNumber("arm extension ticks", getTicks());
    SmartDashboard.putString("arm ret limit switch", getRetractLimitSwitchState().toString());
    SmartDashboard.putBoolean("forwardSwitch", m_extensionMotor
        .getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen).isLimitSwitchEnabled());
  }

  @Override
  public void extendRetract(double speed) {
    if ((speed < 0) && (getPosition() <= m_minExtension)) {
      m_extensionMotor.set(0);
    } else if ((speed > 0) && (getPosition() >= m_maxExtension)) {
      m_extensionMotor.set(0);
    } else {
      m_extensionMotor.set(speed);
    }
  }

  // 100 ticks = 1 inch
  @Override
  public double getPosition() {
    return (m_extensionMotor.getEncoderPositionTicks() - m_zeroOffset) / 311;
  }

  public double getTicks() {
    return m_extensionMotor.getEncoderPositionTicks();
  }

  @Override
  public void setZeroOffset() {
    m_zeroOffset = m_extensionMotor.getEncoderPositionTicks() + 75;
  }

  @Override
  public RetractLimitSwitchState getRetractLimitSwitchState() {
    return m_extensionMotor.isReverseLimitSwitchOn() ? RetractLimitSwitchState.CLOSED
        : RetractLimitSwitchState.OPEN;
  }

  @Override
  public void retractArmInitialize() {
    if (getRetractLimitSwitchState() == RetractLimitSwitchState.CLOSED) {
      m_extensionMotor.set(0);
    } else {
      m_extensionMotor.set(-0.25);
    }
  }

  @Override
  public boolean isInitialized() {
    return m_isInitialized;
  }

  @Override
  public void setInitialized() {
    m_isInitialized = true;
  }
}
