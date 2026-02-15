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
    // we don't really need this because we do it in the command
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
    return getRotatorInRotations() * 360;
  }

  @Override
  public double getRotatorInRotations() {
    return m_rotator.getAbsoluteEncoderPosition();
  }

  /**
   ** This is only for the rotation commands. The encoders only go from 0-360, with
   * it wrapping around. This is an issue because we want the min to be ~-45
   * degrees and the max ~225 degrees. Because of this, I decided to add a method
   * to return the angle but with an offset, so 0 is 90, and -45 is 45. It still
   * goes from 0-360. There's probably a much better way to do this. The way it is
   * done here, the min/max in the config either A. need to be offset by 90
   * degrees or B. need to have 90 added to them in the command. Neither solution
   * is very good, but eh whatever. This is NOT used to PID or anything, its only
   * purpose is to make sure we don't go too far. Meaning, it *should* be fine.
   * Currently, we are using solution A.
   */
  @Override
  public double getRotatorWithOffset() {
    double output = getRotatorAngle();
    output += 90;
    if (output > 360) {
      output -= 360;
    }
    return output;
  }

  @Override
  public void moveExtender(double speed) {
    m_extender.setSpeed(speed);
  }

  @Override
  public double getExtenderPosition() {
    return m_extender.getAbsoluteEncoderPosition();
  }

  @Override
  public void stop() {
    m_extender.setSpeed(0);
    m_rotator.setSpeed(0);
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
