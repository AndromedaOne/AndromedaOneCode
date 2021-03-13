// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.drivetrain;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkController;

public class RomiDriveTrain extends RealDriveTrain {
  private final SparkController m_leftMotor;
  private final SparkController m_rightMotor;

  private final SpeedControllerGroup m_left;
  private final SpeedControllerGroup m_right;

  private final double m_ticksPerInch;

  private double maxSpeed = 0;
  private double lastPosition;
  private double lastVelocity = 0;
  private double maxAcceleration = 0;

  private long lastInstant = System.currentTimeMillis();

  public RomiDriveTrain() {
    Config drivetrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    m_leftMotor = new SparkController(drivetrainConfig, "left");
    m_rightMotor = new SparkController(drivetrainConfig, "right");

    m_left = new SpeedControllerGroup(m_leftMotor);
    m_right = new SpeedControllerGroup(m_rightMotor);
    lastPosition = getRobotPositionInches();
    double ticksPerRevolution = drivetrainConfig.getInt("ticksPerRevolution");
    double wheelDiameterInch = drivetrainConfig.getDouble("wheelDiameterInch");
    m_ticksPerInch = ticksPerRevolution / (wheelDiameterInch * Math.PI);

    SmartDashboard.putNumber("Romi Ticks Per inch", m_ticksPerInch);
    SmartDashboard.putNumber("Romi Ticks Per Revolution", ticksPerRevolution);
    SmartDashboard.putNumber("Romi wheel Diameter", wheelDiameterInch);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  protected SpeedControllerGroup getLeftSpeedControllerGroup() {
    return m_left;
  }

  @Override
  protected SpeedControllerGroup getRightSpeedControllerGroup() {
    return m_right;
  }

  @Override
  public double getRobotPositionInches() {
    double encoderPositionAvg = 0;
    int encoders = 0;
    if (m_leftMotor.hasEncoder()) {
      ++encoders;
      encoderPositionAvg += m_leftMotor.getEncoderPositionTicks();
      SmartDashboard.putNumber("Romi left encoder", m_leftMotor.getEncoderPositionTicks());
    }
    if (m_rightMotor.hasEncoder()) {
      ++encoders;
      encoderPositionAvg += m_rightMotor.getEncoderPositionTicks();
      SmartDashboard.putNumber("Romi right encoder", m_rightMotor.getEncoderPositionTicks());
    }
    if (encoders > 0) {
      encoderPositionAvg = encoderPositionAvg / (m_ticksPerInch * encoders);
    }
    return encoderPositionAvg;
  }

  @Override
  public double getRobotVelocityInches() {

    // TODO Auto-generated method stub
    double currentPosition = getRobotPositionInches();
    double deltaPosition = currentPosition - lastPosition;
    double velocity = deltaPosition / .02;
    long currentInstant = System.currentTimeMillis();
    long deltaInstant = currentInstant - lastInstant;
    lastInstant = currentInstant;
    lastPosition = currentPosition;
    SmartDashboard.putNumber("Romi Delta Instant", deltaInstant);
    if (velocity > maxSpeed) {
      maxSpeed = velocity;
    }
    SmartDashboard.putNumber("Romi Current Velocity", velocity);
    SmartDashboard.putNumber("Romi Max Speed", maxSpeed);

//Acceleration
    double currentVelocity = velocity;
    double deltaVelocity = currentVelocity - lastVelocity;
    double acceleration = deltaVelocity / .02;
    lastVelocity = currentVelocity;

    if (acceleration > maxAcceleration) {
      maxAcceleration = acceleration;
    }
    SmartDashboard.putNumber("Romi Current Acceleration", acceleration);
    SmartDashboard.putNumber("Romi Max Acceleration", maxAcceleration);

    return velocity;
  }

}
