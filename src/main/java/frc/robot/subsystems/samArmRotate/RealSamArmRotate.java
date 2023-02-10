// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmRotate;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealSamArmRotate extends SamArmRotateBase {

  private final SparkMaxController m_motor1;
  // private final SparkMaxController m_motor2;

  /** Creates a new RealSamArmRotate. */
  public RealSamArmRotate() {
    Config armrotateConfig = Config4905.getConfig4905().getSamArmRotateConfig();

    m_motor1 = new SparkMaxController(armrotateConfig, "motor1");
    // m_motor2 = new SparkMaxController(armrotateConfig, "motor2");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void rotate(double speed) {
    m_motor1.set(speed);
  }

  @Override
  public double getAngle() {
    return m_motor1.getEncoderPositionTicks();
  }
}
