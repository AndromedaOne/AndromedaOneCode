// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.actuators.SwerveModule;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.typesafe.config.Config;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;

/** Add your docs here. */
public class KrakenSwerveModule extends SwerveModuleBase {
  private CANcoder m_angleMotorEncoder;
  private TalonFX m_angleMotor;
  private TalonFX m_driveMotor;
  private TalonFXConfiguration m_driveConfiguration;
  private TalonFXConfiguration m_angleConfiguration;
  private double m_lastAngle = 0;
  private Config m_config;
  private double m_driveMotorPositionOffset = 0;
  private int m_moduleNumber;
  private PositionVoltage m_angleSetter;

  // The drive motor code is different because it uses the krakens
  // The angle motor code is the same because it uses the SparkMaxes
  public KrakenSwerveModule(int moduleNumber) {
    super(moduleNumber);
    m_moduleNumber = moduleNumber;
    m_config = Config4905.getConfig4905().getSwerveDrivetrainConfig()
        .getConfig("SwerveDriveConstants");
    /* Angle Motor Config */
    m_angleMotorEncoder = new CANcoder(
        m_config.getInt("ports.Mod" + getModuleNumber() + ".angleMotorEncoderID"), "rio");

    m_angleMotor = new TalonFX(m_config.getInt("ports.Mod" + getModuleNumber() + ".angleMotorID"),
        "rio");

    m_angleConfiguration = new TalonFXConfiguration();
    configAngleMotor();

    /* drive motor config */
    m_driveMotor = new TalonFX(m_config.getInt("ports.Mod" + getModuleNumber() + ".driveMotorID"),
        "rio");
    m_driveConfiguration = new TalonFXConfiguration();
    configDriveMotor();
  }

  private void configDriveMotor() {
    m_driveConfiguration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = m_config
        .getDouble("drivekRampRate");
    if (m_config.getBoolean("driveInvert")) {
      m_driveConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    } else {
      m_driveConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    }

    // The Kraken cannot have a set conversion factor for getPosition and
    // getVelocity
    m_driveMotor.getConfigurator().apply(m_driveConfiguration, 0.1);
    m_driveMotorPositionOffset = m_driveMotor.getPosition().getValueAsDouble();
    
  }

  private void configAngleMotor() {
    m_angleConfiguration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = m_config
        .getDouble("anglekRampRate");
    if (m_config.getBoolean("angleInvert")) {
      m_angleConfiguration.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    } else {
      m_angleConfiguration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
    }

    // The Kraken cannot have a set conversion factor for getPosition and
    // getVelocity
    m_angleSetter = new PositionVoltage(0.0).withSlot(0).withUpdateFreqHz(0);
    m_angleConfiguration.Slot0 = new Slot0Configs().withKP(1).withKI(0.0).withKD(0);
    m_angleConfiguration.ClosedLoopGeneral.ContinuousWrap = true;
    m_angleConfiguration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
    m_angleConfiguration.Feedback.FeedbackRemoteSensorID = m_config.getInt("ports.Mod" + getModuleNumber() + ".angleMotorEncoderID");
    m_angleMotor.getConfigurator().apply(m_angleConfiguration, 0.1);
    
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
    double angle = desiredState.angle.getRotations();
    if (!override
        && Math.abs(desiredState.speedMetersPerSecond) <= (m_config.getDouble("maxSpeed") * 0.01)) {
      angle = m_lastAngle;
    }
    m_lastAngle = angle;
    // need to invert the angle because inverting the motor through the motor
    // controller causes issues with the onboard pidcontroller
    SmartDashboard.putNumber("swervedrive/steerangle/" + m_moduleNumber, angle);
    m_angleMotor.setControl(m_angleSetter.withPosition(angle));
  }

  @Override
  protected double getAngleMotorRawAngle() {
    double angle = m_angleMotorEncoder.getAbsolutePosition().getValueAsDouble() * 360;
    SmartDashboard.putNumber("CanCoder " + m_moduleNumber, m_angleMotorEncoder.getAbsolutePosition().getValueAsDouble());
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

    NeutralModeValue kMode = NeutralModeValue.Brake;
    if (value) {

      kMode = NeutralModeValue.Coast;
    }
    m_driveMotor.setNeutralMode(kMode);
    m_angleMotor.setNeutralMode(kMode);
  }

  @Override
  public void disableAccelerationLimiting() {
    m_driveConfiguration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = 0;
  }

  @Override
  public void enableAccelerationLimiting() {
    m_driveConfiguration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod = m_config
        .getDouble("drivekRampRate");
  }
}