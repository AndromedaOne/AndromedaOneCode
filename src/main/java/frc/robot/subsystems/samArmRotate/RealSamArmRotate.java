// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmRotate;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.sensors.limitswitchsensor.LimitSwitchSensor;
import frc.robot.sensors.limitswitchsensor.RealLimitSwitchSensor;

public class RealSamArmRotate extends SamArmRotateBase {

  private final SparkMaxController m_motor1;
  private final LimitSwitchSensor m_frontAngleLimit;
  private final LimitSwitchSensor m_backAngleLimit;
  private final LimitSwitchSensor m_straightUpSwitch;
  private boolean m_initialized = false;
  private double m_offset = 0;
  private double m_minAngle = 0;
  private double m_maxAngle = 0;
  // private final SparkMaxController m_motor2;

  /** Creates a new RealSamArmRotate. */
  public RealSamArmRotate() {
    Config armrotateConfig = Config4905.getConfig4905().getSamArmRotateConfig();

    m_motor1 = new SparkMaxController(armrotateConfig, "motor1");
    // m_motor2 = new SparkMaxController(armrotateConfig, "motor2");
    m_frontAngleLimit = new RealLimitSwitchSensor("frontAngleLimitSwitch");
    m_backAngleLimit = new RealLimitSwitchSensor("backAngleLimitSwitch");
    m_straightUpSwitch = new RealLimitSwitchSensor("straightUpLimitSwitch");
    m_maxAngle = armrotateConfig.getDouble("maxAngle");
    m_minAngle = armrotateConfig.getDouble("minAngle");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  // Positive speed rotates to the front, negative to the back.
  @Override
  public void rotate(double speed) {
    if ((speed < 0) && (getBackAngleLimitSwitchState() || (getAngle() <= m_minAngle))) {
      m_motor1.set(0);
    }
  }

  @Override
  public double getAngle() {
    return m_motor1.getEncoderPositionTicks() + m_offset;
  }

  @Override
  public boolean getInitialized() {
    return m_initialized;
  }

  @Override
  public void setInitialized() {
    m_initialized = true;
    m_offset = m_motor1.getEncoderPositionTicks();
  }

  @Override
  public boolean getFrontAngleLimitSwitchState() {
    return m_frontAngleLimit.isAtLimit();
  }

  @Override
  public boolean getBackAngleLimitSwitchState() {
    return m_backAngleLimit.isAtLimit();
  }

  @Override
  public boolean getStraightUpLimitSwitchState() {
    return m_straightUpSwitch.isAtLimit();
  }
}
