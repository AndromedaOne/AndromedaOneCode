// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.sensors.limitswitchsensor.RealLimitSwitchSensor;

public class RealSamArmExtRet extends SamArmExtRetBase {

  private final SparkMaxController m_extensionMotor;
  private final LimitSwitchSensor m_retractLimitSwitch;
  private double m_zeroOffset = 0;
  private double m_maxExtension = 0;
  private double m_minExtension = 0;

  /** Creates a new RealSamArmExtension. */
  public RealSamArmExtRet() {
    Config armextensionConfig = Config4905.getConfig4905().getSamArmExtensionConfig();

    m_extensionMotor = new SparkMaxController(armextensionConfig, "extensionmotor");
    m_retractLimitSwitch = new RealLimitSwitchSensor("retractLimitSwitch");
    m_maxExtension = armextensionConfig.getDouble("maxExtension");
    m_minExtension = armextensionConfig.getDouble("minExtension");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("arm extention position", getPosition());
  }

  @Override
  public void extendRetract(double speed) {
    if ((speed < 0) && (getRetractLimitSwitchState() || (getPosition() <= m_minExtension))) {
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
    return (m_extensionMotor.getEncoderPositionTicks() - m_zeroOffset) / 100;
  }

  @Override
  public boolean getRetractLimitSwitchState() {
    return m_retractLimitSwitch.isAtLimit();
  }

  @Override
  public void setZeroOffset() {
    m_zeroOffset = m_extensionMotor.getEncoderPositionTicks();
  }
}
