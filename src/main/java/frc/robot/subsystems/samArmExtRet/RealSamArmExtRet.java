// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealSamArmExtRet extends SamArmExtRetBase {

  private final SparkMaxController m_extensionMotor;

  /** Creates a new RealSamArmExtension. */
  public RealSamArmExtRet() {
    Config armextensionConfig = Config4905.getConfig4905().getSamArmExtensionConfig();

    m_extensionMotor = new SparkMaxController(armextensionConfig, "extensionmotor");
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
    return m_extensionMotor.getEncoderPositionTicks();
  }
}
