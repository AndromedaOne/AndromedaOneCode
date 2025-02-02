// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armTestBed;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Radian;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.typesafe.config.Config;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.measure.MutAngle;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.sensors.powerDistribution4905.PowerDistributionBase;

/**
 * 0 angle is poining straight down
 */
public class RealArmTestBed extends SubsystemBase implements ArmTestBedBase {
  private final SparkMaxController m_motor;
  private final double m_minAngleRad = -Math.PI / 4; // -45 degrees
  private final double m_maxAngleRad = 3 * Math.PI / 4;// 135 degrees
  private final double m_angleOffset = 1 - 0.28;
  private double m_maxSpeed = 0.5;
  private double m_kP = 1.53;
  private double m_kI = 0.0;
  private double m_kD = 0.0;
  private double m_kG = 0.106;
  private double m_kVoltage = 0.0;
  private double m_voltageReference = 12.5;
  private double m_currentVoltage = 0.0;
  private PowerDistributionBase m_powerDistribution;
  private final MutVoltage m_appliedVoltage = Volts.mutable(0);
  private final MutAngle m_angle = Radian.mutable(0);
  private final MutAngularVelocity m_velocity = RadiansPerSecond.mutable(0);
  private final SysIdRoutine m_sysIdRoutine;
  private double m_currentAngularVelRad = 0.0;
  private double m_previousTimeAngularVel = 0.0;
  private double m_previousAngleAngularVelRad = m_minAngleRad;
  private PIDController4905 m_controller = new PIDController4905("Arm Test Bed PID", m_kP, m_kI,
      m_kD, 0);

  public RealArmTestBed() {
    SmartDashboard.putNumber("kG", m_kG);
    SmartDashboard.putNumber("kP", m_kP);
    SmartDashboard.putNumber("kVoltage", m_kVoltage);
    Config armrotateConfig = Config4905.getConfig4905().getArmTestBedConfig();
    m_controller.enableContinuousInput(-Math.PI, Math.PI);
    m_motor = new SparkMaxController(armrotateConfig, "motor", false, false);
    m_sysIdRoutine = new SysIdRoutine(new SysIdRoutine.Config(),
        new SysIdRoutine.Mechanism(this::setVoltage, log -> {
          log.motor("Arm Test Bed")
              .voltage(m_appliedVoltage.mut_replace(m_motor.getVoltage(), Volts))
              .angularPosition(m_angle.mut_replace(getAngleDeg(), Degrees))
              .angularVelocity(m_velocity.mut_replace(getAngularVelRad(), DegreesPerSecond));
        }, this.getSubsystemBase()));
    m_powerDistribution = Robot.getInstance().getSensorsContainer().getPowerDistributionBase();
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

  private double calculateCorrectedEncoder() {
    double correctedEncoderValue = (1 - m_motor.getAbsoluteEncoderPosition());
    if (correctedEncoderValue >= m_angleOffset) {
      correctedEncoderValue = correctedEncoderValue - m_angleOffset;
    } else {
      correctedEncoderValue = correctedEncoderValue + (1 - m_angleOffset);
    }
    if (correctedEncoderValue > 0.5) {
      correctedEncoderValue = correctedEncoderValue - 1;
    }
    SmartDashboard.putNumber("Corrected Encoder Value", correctedEncoderValue);
    return correctedEncoderValue;
  }

  @Override
  public double getAngleDeg() {
    double angle = (calculateCorrectedEncoder() * 360);
    return angle;
  }

  @Override
  public double getAngleRad() {
    double angle = calculateCorrectedEncoder() * 2 * Math.PI;
    return angle;
  }

  private void updateAngularVelocityRad() {
    long currentTime = System.currentTimeMillis();
    double currentAngleRad = getAngleRad();
    SmartDashboard.putNumber("Delta angle:", currentAngleRad - m_previousAngleAngularVelRad);
    SmartDashboard.putNumber("Delta time:", currentTime - m_previousTimeAngularVel);
    m_currentAngularVelRad = ((currentAngleRad - m_previousAngleAngularVelRad)
        / (currentTime - m_previousTimeAngularVel)) * 1000;
    m_previousTimeAngularVel = currentTime;
    m_previousAngleAngularVelRad = currentAngleRad;
  }

  @Override
  public double getAngularVelRad() {
    return m_currentAngularVelRad;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Arm Test Bed Angle in Degrees", getAngleDeg());
    updateAngularVelocityRad();
    SmartDashboard.putNumber("Arm Test Bed Angle in Rads", getAngleRad());
    SmartDashboard.putNumber("Encoder Position", m_motor.getAbsoluteEncoderPosition());
    SmartDashboard.putNumber("Arm Test Bed Angular Velocity", getAngularVelRad());
    SmartDashboard.putNumber("position error", m_controller.getPositionError());
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
    MathUtil.clamp(speed, -m_maxSpeed, m_maxSpeed);
    if ((getAngleRad() <= m_minAngleRad) && (speed < 0)) {
      stop();
    } else if ((getAngleRad() >= m_maxAngleRad) && (speed > 0)) {
      stop();
    } else {
      m_motor.setSpeed(speed);
    }
    SmartDashboard.putNumber("Speed: ", speed);
  }

  public void setVoltage(Voltage voltage) {
    if ((getAngleRad() <= m_minAngleRad) && (voltage.in(Volts) < 0)) {
      stop();
    } else if ((getAngleRad() >= m_maxAngleRad) && (voltage.in(Volts) > 0)) {
      stop();
    } else {
      m_motor.setVoltage(voltage);
    }
  }

  @Override
  public Command sysIdQuasistatic(Direction direction) {
    return m_sysIdRoutine.quasistatic(direction);
  }

  @Override
  public Command sysIdDynamic(Direction direction) {
    return m_sysIdRoutine.dynamic(direction);
  }

  @Override
  public void setGoalDeg(double goal) {
    m_controller.setSetpoint((goal) * Math.PI / 180);
    m_kG = SmartDashboard.getNumber("kG", m_kG);
    m_kP = SmartDashboard.getNumber("kP", m_kP);
    m_kVoltage = SmartDashboard.getNumber("kVoltage", m_kVoltage);
    m_controller.setP(m_kP);
  }

  @Override
  public void calculateSpeed() {
    m_currentVoltage = m_powerDistribution.getVoltage();
    double voltageCorrection = (m_voltageReference - m_currentVoltage) *m_kVoltage; 
    if (m_controller.getPositionError()< 0){
      voltageCorrection = -voltageCorrection;
    }
    double currentAngleRad = getAngleRad();
    double pidCalc = m_controller.calculate(currentAngleRad);
    double feedforwardCalc = m_kG * Math.cos(getAngleRad());
    double speed = pidCalc + feedforwardCalc + voltageCorrection;
    rotate(speed);
    SmartDashboard.putNumber("Error", m_controller.getPositionError());
    SmartDashboard.putNumber("pidCalc", pidCalc);
    SmartDashboard.putNumber("feedForwardCalc", feedforwardCalc);
    SmartDashboard.putNumber("Current setpoint:", m_controller.getSetpoint());
    SmartDashboard.putNumber("Voltage correction", voltageCorrection);
    SmartDashboard.putNumber("Current voltage", m_currentVoltage);
  }
}