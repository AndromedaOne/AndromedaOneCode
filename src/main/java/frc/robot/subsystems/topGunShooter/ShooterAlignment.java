// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunShooter;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.HitecHS322HDpositionalServoMotor;
import frc.robot.actuators.ServoMotorPositional;
import frc.robot.actuators.SparkMaxController;

public class ShooterAlignment extends SubsystemBase implements ShooterAlignmentBase {
  SparkMaxController m_angleMotor;
  ServoMotorPositional m_hooksServoMotor;
  Config m_shooterConfig = Config4905.getConfig4905().getShooterConfig();
  private double m_offset = 0;
  private boolean m_initialized = false;

  /** Creates a new ShooterAlignment. */
  public ShooterAlignment() {
    m_angleMotor = new SparkMaxController(m_shooterConfig, getShooterName());
    m_hooksServoMotor = new HitecHS322HDpositionalServoMotor(m_shooterConfig, "hooksServoMotor");
  }

  public void rotateShooter(double speed) {
    m_angleMotor.setSpeed(speed);
  }

  public boolean atTopLimitSwitch() {
    return m_angleMotor.isForwardLimitSwitchOn();
  }

  public boolean atBottomLimitSwitch() {
    return m_angleMotor.isReverseLimitSwitchOn();
  }

  public void setCoastMode() {
    m_angleMotor.setCoastMode();
  }

  public void setBrakeMode() {
    m_angleMotor.setBrakeMode();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("ShooterAlignmentLimitSwitchTop", atTopLimitSwitch());
    SmartDashboard.putBoolean("ShooterAlignmentLimitSwitchBottom", atBottomLimitSwitch());
    SmartDashboard.putNumber("ShooterAlignmentAngle", getAngle());
  }

  @Override
  public double getAngle() {
    return m_angleMotor.getEncoderPositionTicks() - getOffset();
  }

  @Override
  public String getShooterName() {
    return "angleMotor";
  }

  @Override
  public void stopShooterAlignment() {
    m_angleMotor.setSpeed(0);
  }

  @Override
  public void extendShooterArms() {
    m_hooksServoMotor.set(1);
  }

  @Override
  public void stowShooterArms() {
    m_hooksServoMotor.set(0);
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
    super.setDefaultCommand(command);
  }

  @Override
  public boolean getInitialized() {
    return m_initialized;
  }

  @Override
  public void setInitialized() {
    m_initialized = true;
  }

  @Override
  public void setOffset(double offset) {
    m_offset = offset;
  }

  @Override
  public double getOffset() {
    return m_offset;
  }
}
