// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.climber;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealClimber extends SubsystemBase implements ClimberBase {

  private SparkMaxController m_extender;
  private SparkMaxController m_rotator;
  private double m_minRotation;
  private double m_maxRotation;

  private Config m_config = Config4905.getConfig4905().getClimberConfig();

  public RealClimber() {

    m_extender = new SparkMaxController(m_config, "extender", false, false);
    m_rotator = new SparkMaxController(m_config, "rotator", false, false);
    m_minRotation = m_config.getDouble("rotator.minAngle");
    m_maxRotation = m_config.getDouble("rotator.maxAngle");
  }

  @Override
  public void rotateRotator(double speed) {
    // assumes pos speed is inc and neg speed is dec
    if ((speed > 0) && (getRotatorAngle() >= m_maxRotation)) {
      m_rotator.setSpeed(0);
    } else if ((speed < 0) && (getRotatorAngle() <= m_minRotation)) {
      m_rotator.setSpeed(0);
    } else {
      m_rotator.setSpeed(speed);
    }
  }

  @Override
  public double getRotatorAngle() {
    return m_rotator.getAbsoluteEncoderPosition();
  }

  @Override
  public void moveExtender(double speed) {
    m_extender.setSpeed(speed);
  }

  @Override
  public void setBrakeMode() {
    m_extender.setBrakeMode();
    m_rotator.setBrakeMode();
  }

  @Override
  public void setCoastMode() {
    m_extender.setCoastMode();
    m_rotator.setCoastMode();
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

}
