// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.SparkMaxController;

public class RealSamArmExtRet extends SamArmExtRetBase {

  private final SparkMaxController m_extensionMotor;
  private double m_zeroOffset = 0;
  private double m_maxExtension = 0;
  private double m_minExtension = 0;
  private boolean m_isInitialized = false;
  private DigitalInput m_retractLimitSwitch = new DigitalInput(9);
  private static final double m_armFrontMaxDistanceFromPivotPoint = 68;
  private static final double m_armRearMaxDistanceFromPivotPoint = 62;
  private static final double m_armLengthOffset = 36;

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
    SmartDashboard.putBoolean("forwardSwitch", m_retractLimitSwitch.get());
  }

  @Override
  public void extendRetract(double speed) {
    if ((speed < 0) && (getPosition() <= m_minExtension)) {
      m_extensionMotor.set(0);
    } else if ((speed > 0) && (getPosition() >= m_maxExtension)
        && ((getPosition() + m_armLengthOffset) > calcMaxExtendDistance())) {
      m_extensionMotor.set(0);
    } else {
      m_extensionMotor.set(speed);
    }
  }

  // 311 ticks = 1 inch
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
    return m_retractLimitSwitch.get() ? RetractLimitSwitchState.CLOSED
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

  private double calcMaxExtendDistance() {
    double angle = Robot.getInstance().getSubsystemsContainer().getArmRotateBase().getAngle();
    if (angle == 90) {
      return Double.MAX_VALUE;
    }
    if (angle < 180) {
      angle = (angle - 90);
      return m_armFrontMaxDistanceFromPivotPoint / Math.cos(angle);
    }
    angle = Math.abs(angle - 270);
    return m_armRearMaxDistanceFromPivotPoint / Math.cos(angle);
  }

}
