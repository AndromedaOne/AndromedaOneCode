// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.armTestBed;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.DegreesPerSecond;
import static edu.wpi.first.units.Units.Radian;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.Rotations;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.Volts;

import org.opencv.core.Mat;

import com.typesafe.config.Config;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.units.VoltageUnit;
import edu.wpi.first.units.measure.MutAngle;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.units.measure.MutVoltage;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/**
 * 0 angle is poining straight down
 */
public class RealArmTestBed extends SubsystemBase implements ArmTestBedBase {
  private final SparkMaxController m_motor;
  private final double m_minAngle = -Math.PI/4;
  private final double m_maxAngle = 3*Math.PI/4;
  private final double m_angleOffset = 1 - 0.28;
  private double kDt = 0.02;
  private double kMaxVelocity = 1.75;
  private double kMaxAcceleration = 0.75;
  private double kP = 0.000001;
  private double kI = 0.0;
  private double kD = 0.0;
  private double kS = 1.0;
  private double kG = 0.18;
  private double kV = 3.5;
  private final MutVoltage m_appliedVoltage = Volts.mutable(0);
  private final MutAngle m_angle = Radian.mutable(0);
  private final MutAngularVelocity m_velocity = RadiansPerSecond.mutable(0);
  private final SysIdRoutine m_sysIdRoutine;
  private double m_currentAngularVelDeg = 0.0;
  private double m_previousTimeAngularVel = 0.0;
  private double m_previousAngleAngularVel = m_minAngle;

  private final TrapezoidProfile.Constraints m_constraints = new TrapezoidProfile.Constraints(
      kMaxVelocity, kMaxAcceleration);
  private final ArmFeedforward m_feedforward = new ArmFeedforward(kS, kV, kG);
  private final ProfiledPIDController m_controller = new ProfiledPIDController(kP, kI, kD,
      m_constraints);

  public RealArmTestBed() {
    Config armrotateConfig = Config4905.getConfig4905().getArmTestBedConfig();
    m_motor = new SparkMaxController(armrotateConfig, "motor", false, false);
    m_sysIdRoutine = new SysIdRoutine(new SysIdRoutine.Config(),
        new SysIdRoutine.Mechanism(this::setVoltage, log -> {
          log.motor("Arm Test Bed")
              .voltage(m_appliedVoltage
                  .mut_replace(m_motor.getVoltage(), Volts))
              .angularPosition(m_angle.mut_replace(getAngleDeg(), Degrees))
              .angularVelocity(m_velocity.mut_replace(getAngularVelDeg(), DegreesPerSecond));
        }, this.getSubsystemBase()));
    
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

  private double calculateCorrectedEncoder(){
    double correctedEncoderValue = (1 -m_motor.getAbsoluteEncoderPosition());
    if(correctedEncoderValue>= m_angleOffset){
      correctedEncoderValue=  correctedEncoderValue - m_angleOffset;
    } else {
      correctedEncoderValue= correctedEncoderValue + (1 - m_angleOffset);
    }
    if ( correctedEncoderValue > 0.5)
    {
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

  private void updateAngularVelocity() {
    long currentTime = System.currentTimeMillis();
    double currentAngle = getAngleDeg();
    SmartDashboard.putNumber("Delta angle:", currentAngle - m_previousAngleAngularVel);
    SmartDashboard.putNumber("Delta time:", currentTime - m_previousTimeAngularVel);
    m_currentAngularVelDeg = ((currentAngle - m_previousAngleAngularVel)
        / (currentTime - m_previousTimeAngularVel))*1000;
    m_previousTimeAngularVel = currentTime;
    m_previousAngleAngularVel = currentAngle;
  }

  @Override
  public double getAngularVelDeg() {
    return m_currentAngularVelDeg;
  }

  @Override
  public double getAngularVelRad() {
    return (m_currentAngularVelDeg * (Math.PI / 180));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Arm Test Bed Angle in Degrees", getAngleDeg());
    updateAngularVelocity();
    SmartDashboard.putNumber("Arm Test Bed Angle in Rads", getAngleRad());
    SmartDashboard.putNumber("Encoder Position", m_motor.getAbsoluteEncoderPosition());
    SmartDashboard.putNumber("Arm Test Bed Angular Velocity", getAngularVelDeg());
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
    if ((getAngleDeg() <= m_minAngle) && (speed < 0)) {
      stop();
    } else if ((getAngleDeg() >= m_maxAngle) && (speed > 0)) {
      stop();
    } else {
      m_motor.setSpeed(speed);
    }
  }

  public void setVoltage (Voltage voltage){
    if ((getAngleDeg() <= m_minAngle) && (voltage.in(Volts) < 0)) {
      stop();
    } else if ((getAngleDeg() >= 155) && (voltage.in(Volts) > 0)) {
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
  public void setGoal(double goal) {
    m_controller.setGoal((goal - 90.0)*Math.PI/180);
  }

  @Override
  public void calculateAndSetVoltageForGoal() {
    double currentAngleRad = getAngleRad() - Math.PI/2;
    double voltage = m_controller.calculate(currentAngleRad) + 
    m_feedforward.calculate(currentAngleRad, getAngularVelRad());
    setVoltage(Volts.mutable(voltage));
    System.out.println("Voltage: " + voltage);
  }
}