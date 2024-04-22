// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.PWM.PeriodMultiplier;

/**
 * Hitec HS-322HD servo provided in the FIRST Kit of Parts in 2008.
 */
public class HitecHS322HDpositionalServoMotor extends ServoMotorPositional {
  // the following are default values for the Hitec HS322HD servo
  private final double m_maxServoAngle = 180.0;
  private final double m_minServoAngle = 0.0;

  private final int m_maxServoPWM = 2400;
  private final int m_minServoPWM = 600;

  public HitecHS322HDpositionalServoMotor(Config motorConfig, String servoName) {
    super(motorConfig, servoName);
    setBoundsMicroseconds(m_maxServoPWM, 0, 0, 0, m_minServoPWM);
    setPeriodMultiplier(PeriodMultiplier.k4X);
    setMinMaxServoAngle(m_minServoAngle, m_maxServoAngle);
  }
}
