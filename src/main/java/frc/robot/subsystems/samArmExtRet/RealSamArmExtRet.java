// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.sensors.limitswitchsensor.RealLimitSwitchSensor;

public class RealSamArmExtRet extends SamArmExtRetBase {

  private final SparkMaxController m_extensionMotor;
  private final LimitSwitchSensor m_retractLimitSwitch;
  private boolean m_initialized = false;
  private double m_offset = 0;

  /** Creates a new RealSamArmExtension. */
  public RealSamArmExtRet() {
    Config armextensionConfig = Config4905.getConfig4905().getSamArmExtensionConfig();

    m_extensionMotor = new SparkMaxController(armextensionConfig, "extensionmotor");
    m_retractLimitSwitch = new RealLimitSwitchSensor("retractLimitSwitch");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void extendRetract(double speed) {
    m_extensionMotor.set(speed);
  }

  @Override
  public double getPosition() {
    return m_extensionMotor.getEncoderPositionTicks() + m_offset;
  }

  @Override
  public boolean getRetractLimitSwitchState() {
    return m_retractLimitSwitch.isAtLimit();
  }

  @Override
  public boolean getInitialized() {
    return m_initialized;
  }

  @Override
  public void setInitialized() {
    m_initialized = true;
    m_offset = m_extensionMotor.getEncoderPositionTicks();
  }
}
