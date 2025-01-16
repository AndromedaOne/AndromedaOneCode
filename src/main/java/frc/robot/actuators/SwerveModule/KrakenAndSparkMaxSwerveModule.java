// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators.SwerveModule;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.SparkBase.ControlType;
import com.typesafe.config.Config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class KrakenAndSparkMaxSwerveModule extends SwerveModuleBase {
  private SparkMaxController m_angleMotor;
  private TalonFX m_driveMotor;
  private TalonFXConfiguration m_configuration;
  private double m_lastAngle = 0;
  private Config m_config;

  private SimpleMotorFeedforward m_feedForward;

  // The drive motor code is different because it uses the krakens
  // The angle motor code is the same because it uses the SparkMaxes
  public KrakenAndSparkMaxSwerveModule(int moduleNumber) {
    super(moduleNumber);
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getConfig("SwerveDriveConstants");
    m_feedForward = new SimpleMotorFeedforward(m_config.getDouble("driveKS"),
        m_config.getDouble("driveKV"), m_config.getDouble("driveKA"));
    /* Angle Motor Config */
    m_angleMotor = new SparkMaxController(m_config, "Mod" + getModuleNumber() + ".angleMotorID",
        true, false);

    /* drive motor config */
    m_driveMotor = new TalonFX(m_config.getInt("ports.Mod" + getModuleNumber() + ".driveMotorID"),
        "rio");
    m_configuration = new TalonFXConfiguration();
    configDriveMotor();
  }

  private void configDriveMotor() {
    m_configuration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = m_config
        .getDouble("drivekRampRate");
    if (m_config.getBoolean("driveInvert")) {
      m_configuration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    } else {
      m_configuration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    }

    // The Kraken cannot have a set conversion factor for getPosition and
    // getVelocity
    m_driveMotor.getConfigurator().apply(m_configuration, 0.1);
    m_driveMotor.setPosition(0.0);
  }

  @Override
  protected void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop) {
    if (isOpenLoop) {
      double percentOutput = desiredState.speedMetersPerSecond / m_config.getDouble("maxSpeed");
      m_driveMotor.set(percentOutput);
    }
  }

  @Override
  protected void setAngle(SwerveModuleState desiredState, boolean override) {
    double angle = desiredState.angle.getDegrees();
    if (!override
        && Math.abs(desiredState.speedMetersPerSecond) <= (m_config.getDouble("maxSpeed") * 0.01)) {
      angle = m_lastAngle;
    }
    m_lastAngle = angle;
    // need to invert the angle because inverting the motor through the motor
    // controller causes issues with the onboard pidcontroller
    if (angle < 0) {
      angle += 360;
    }
    m_angleMotor.getMotorController().getClosedLoopController().setReference(angle,
        ControlType.kPosition);
  }

  @Override
  protected double getAngleMotorRawAngle() {
    double angle = m_angleMotor.getAbsoluteEncoderPosition();
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  @Override
  public double getDriveEncoderPosition() {
    double positionConversionFactor = m_config.getDouble("wheelDiameter") * Math.PI
        / m_config.getDouble("driveGearRatio");
    return m_driveMotor.getPosition().getValueAsDouble() * (positionConversionFactor / 39.3701);
  }

  @Override
  public double getDriveEncoderVelocity() {
    double positionConversionFactor = m_config.getDouble("wheelDiameter") * Math.PI
        / m_config.getDouble("driveGearRatio");
    return m_driveMotor.getVelocity().getValueAsDouble()
        * ((positionConversionFactor * 39.3701) / 60);
  }

  @Override
  public void setCoast(boolean value) {

    NeutralModeValue kMode = NeutralModeValue.Brake;
    if (value) {
      m_angleMotor.setCoastMode();
      kMode = NeutralModeValue.Coast;
    } else {
      m_angleMotor.setBrakeMode();
    }
    m_driveMotor.setNeutralMode(kMode);
  }

  @Override
  public void disableAccelerationLimiting() {
    m_configuration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0;
  }

  @Override
  public void enableAccelerationLimiting() {
    m_configuration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = m_config
        .getDouble("drivekRampRate");
  }
}