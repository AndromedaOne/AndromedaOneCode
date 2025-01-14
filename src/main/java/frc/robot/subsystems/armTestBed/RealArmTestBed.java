// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armTestBed;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/**
 * 0 angle is poining straight down
 */
public class RealArmTestBed extends SubsystemBase implements ArmTestBedBase {
  private final SparkMaxController m_motor;
  private double m_minAngle = 0;
  private double m_maxAngle = 0;
  private double kDt = 0.02;
  private double kMaxVelocity = 1.75;
  private double kMaxAcceleration = 0.75;
  private double kP = 1.3;
  private double kI = 0.0;
  private double kD = 0.7;
  private double kS = 1.1;
  private double kG = 1.2;
  private double kV = 1.3;
  private Encoder m_encoder = new Encoder(1, 0);

  public RealArmTestBed() {
    Config armrotateConfig = Config4905.getConfig4905().getArmTestBedConfig();
    m_motor = new SparkMaxController(armrotateConfig, "motor", false, false);
    m_encoder = new Encoder(1, 2);
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  @Override
  public void setPosition(double angle) {

  }

  @Override
  public void stop() {
    m_motor.setSpeed(0);
  }

  @Override
  public double getAngle() {
    return 360 - (m_motor.getAbsoluteEncoderPosition() * 360);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Arm Test Bed Angle", getAngle());
  }

  @Override
  public void setCoastMode() {
    m_motor.setCoastMode();
  }

  @Override
  public void setBrakeMode() {
    m_motor.setBrakeMode();
  }

}