// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armTestBed;

import com.typesafe.config.Config;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
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
  private final double m_minAngle = 45;
  private final double m_maxAngle = 315;
  private double kDt = 0.02;
  private double kMaxVelocity = 1.75;
  private double kMaxAcceleration = 0.75;
  private double kP = 1.3;
  private double kI = 0.0;
  private double kD = 0.7;
  private double kS = 1.1;
  private double kG = 1.2;
  private double kV = 1.3;
  private final TrapezoidProfile.Constraints m_constraints = new TrapezoidProfile.Constraints(
      kMaxVelocity, kMaxAcceleration);
  private final ArmFeedforward m_feedforward = new ArmFeedforward(kS, kV, kG);
  private final ProfiledPIDController m_controller = new ProfiledPIDController(kP, kI, kD,
      m_constraints);

  public RealArmTestBed() {
    Config armrotateConfig = Config4905.getConfig4905().getArmTestBedConfig();
    m_motor = new SparkMaxController(armrotateConfig, "motor", false, false);
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
    double angle = 360 - (m_motor.getAbsoluteEncoderPosition() * 360);
    if (angle >= 171) {
      angle -= 171;
    } else {
      angle += 190;
    }
    return angle;

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

  @Override
  public void rotate(double speed) {
    if ((getAngle() <= m_minAngle) && (speed < 0)) {
      stop();
    } else if ((getAngle() >= m_maxAngle) && (speed > 0)) {
      stop();
    } else {
      m_motor.setSpeed(speed);
    }

  }

}