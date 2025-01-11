// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators.SwerveModule;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkAbsoluteEncoder;
import com.revrobotics.SparkPIDController;
import com.typesafe.config.Config;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.utils.CANSparkMaxUtil;
import frc.robot.utils.CANSparkMaxUtil.Usage;

/** Add your docs here. */
public class KrakenAndSparkMaxSwerveModule extends SwerveModuleBase {
  private SparkMaxController m_angleMotor;
  private TalonFX m_driveMotor;
  private TalonFXConfiguration m_configuration;
  private SparkPIDController m_angleController;
  private double m_lastAngle = 0;
  private Config m_config;
  private double m_driveMotorPositionOffset = 0;

  private AbsoluteEncoder m_absoluteAngleEncoder;

  // The drive motor code is different because it uses the krakens
  // The angle motor code is the same because it uses the SparkMaxes
  public KrakenAndSparkMaxSwerveModule(int moduleNumber) {
    super(moduleNumber);
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getConfig("SwerveDriveConstants");
    new SimpleMotorFeedforward(m_config.getDouble("driveKS"), m_config.getDouble("driveKV"),
        m_config.getDouble("driveKA"));
    /* Angle Motor Config */
    m_angleMotor = new SparkMaxController(m_config, "Mod" + getModuleNumber() + ".angleMotorID");
    configAngleMotor();

    /* drive motor config */
    m_driveMotor = new TalonFX(m_config.getInt("ports.Mod" + getModuleNumber() + ".driveMotorID"),
        "rio");
    m_configuration = new TalonFXConfiguration();
    configDriveMotor();
  }

  private void configAngleMotor() {
    m_angleMotor.getMotorController().restoreFactoryDefaults();
    m_absoluteAngleEncoder = m_angleMotor.getMotorController()
        .getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);
    m_angleController = m_angleMotor.getMotorController().getPIDController();
    CANSparkMaxUtil.setCANSparkMaxBusUsage(m_angleMotor.getMotorController(), Usage.kPositionOnly);
    m_angleMotor.getMotorController()
        .setSmartCurrentLimit(m_config.getInt("angleContinuousCurrentLimit"));
    m_absoluteAngleEncoder.setPositionConversionFactor(
        m_config.getInt("angleDegreesPerRotation") / m_config.getInt("angleGearRatio"));
    // since we're using an abolute encoder for motor angle, need to set that as the
    // input
    // to the onboard PID controller
    m_angleController.setFeedbackDevice(m_absoluteAngleEncoder);
    m_angleController.setP(m_config.getDouble("angleKP"));
    m_angleController.setI(m_config.getDouble("angleKI"));
    m_angleController.setD(m_config.getDouble("angleKD"));
    m_angleController.setFF(m_config.getDouble("angleKFF"));
    m_angleController.setPositionPIDWrappingEnabled(true);
    m_angleController.setPositionPIDWrappingMinInput(0);
    m_angleController.setPositionPIDWrappingMaxInput(360);
    m_angleController.setOutputRange(-1.0, 1.0);

    m_angleMotor.getMotorController().enableVoltageCompensation(m_config.getDouble("voltageComp"));
    m_angleMotor.getMotorController().setInverted(m_config.getBoolean("angleInvert"));
    m_angleMotor.getMotorController().setClosedLoopRampRate(m_config.getDouble("anglekRampRate"));
    m_absoluteAngleEncoder.setInverted(m_config.getBoolean("absoluteAngleEncoderInvert"));
    m_angleMotor.getMotorController().burnFlash();

  }

  private void configDriveMotor() {
    m_driveMotor.setInverted(m_config.getBoolean("driveInvert"));
    m_configuration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = m_config
        .getDouble("drivekRampRate");
    // The Kraken cannot have a set conversion factor for getPosition and
    // getVelocity
    m_driveMotor.getConfigurator().apply(m_configuration, 0.1);
    m_driveMotorPositionOffset = m_driveMotor.getPosition().getValueAsDouble();
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
    m_angleController.setReference(angle, ControlType.kPosition);
  }

  @Override
  protected double getAngleMotorRawAngle() {
    double angle = m_absoluteAngleEncoder.getPosition();
    if (angle < 0) {
      angle += 360;
    }
    return angle;
  }

  @Override
  public double getDriveEncoderPosition() {
    double positionConversionFactor = m_config.getDouble("wheelDiameter") * Math.PI
        / m_config.getDouble("driveGearRatio");
    return (m_driveMotor.getPosition().getValueAsDouble() - m_driveMotorPositionOffset)
        * (positionConversionFactor / 39.3701);
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
    IdleMode mode = CANSparkMax.IdleMode.kBrake;
    NeutralModeValue kMode = NeutralModeValue.Brake;
    if (value) {
      mode = CANSparkMax.IdleMode.kCoast;
      kMode = NeutralModeValue.Coast;
    }

    m_angleMotor.setIdleMode(mode);
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