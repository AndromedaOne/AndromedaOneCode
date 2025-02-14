// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdArm;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

/** Add your docs here. */
public class RealSBSDArm extends SubsystemBase implements SBSDArmBase {

  private SparkMaxController m_leftAngleMotor;
  private SparkMaxController m_rightAngleMotor;
  private DoubleSupplier m_absoluteEncoderPosition;
  private double m_minAngleDeg = 0.0;
  private double m_maxAngleDeg = 0.0;
  private double m_angleOffset = 0.0;
  private double m_safetyAngle = 0.0;
  private double m_maxSpeed = 0.0;
  private double m_kP = 0.0;
  private double m_kI = 0.0;
  private double m_kD = 0.0;
  private double m_kG = 0.0;
  private double m_tolerance = 0.0;
  private PIDController4905 m_controller;
  private CoralEndEffectorRotateBase m_endEffector;

  public RealSBSDArm() {
    Config armrotateConfig = Config4905.getConfig4905().getSBSDArmConfig();
    m_rightAngleMotor = new SparkMaxController(armrotateConfig, "armAngleRight", false, false);
    m_leftAngleMotor = new SparkMaxController(armrotateConfig, "armAngleLeft", false, false);
    m_absoluteEncoderPosition = () -> m_rightAngleMotor.getAbsoluteEncoderPosition();
    m_angleOffset = armrotateConfig.getDouble("angleOffset");
    m_minAngleDeg = armrotateConfig.getDouble("minAngleDeg");
    m_maxAngleDeg = armrotateConfig.getDouble("maxAngleDeg");
    m_safetyAngle = armrotateConfig.getDouble("safetyAngle");
    m_maxSpeed = armrotateConfig.getDouble("maxSpeed");
    m_kP = armrotateConfig.getDouble("kP");
    m_kI = armrotateConfig.getDouble("kI");
    m_kD = armrotateConfig.getDouble("kD");
    m_kG = armrotateConfig.getDouble("kG");
    SmartDashboard.putNumber("SBSD Arm kG", m_kG);
    SmartDashboard.putNumber("SBSD Arm kP", m_kP);
    m_tolerance = armrotateConfig.getDouble("tolerance");
    m_controller = new PIDController4905("SBSD Arm PID", m_kP, m_kI, m_kD, 0);
    m_controller.enableContinuousInput(-Math.PI, Math.PI);
    m_controller.setTolerance(Math.toRadians(m_tolerance));
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
    m_leftAngleMotor.setSpeed(0);
    m_rightAngleMotor.setSpeed(0);
  }

  private double calculateCorrectedEncoder() {
    double correctedEncoderValue = (m_absoluteEncoderPosition.getAsDouble());
    if (correctedEncoderValue >= m_angleOffset) {
      correctedEncoderValue = correctedEncoderValue - m_angleOffset;
    } else {
      correctedEncoderValue = correctedEncoderValue + (1 - m_angleOffset);
    }
    if (correctedEncoderValue > 0.5) {
      correctedEncoderValue = correctedEncoderValue - 1;
    }
    SmartDashboard.putNumber("SBSD Arm Corrected Encoder Value", correctedEncoderValue);
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

  @Override
  public void periodic() {
    SmartDashboard.putNumber("SBSD Arm Angle in Degrees", getAngleDeg());
    SmartDashboard.putNumber("SBSD Arm Angle in Rads", getAngleRad());
    SmartDashboard.putNumber("SBSD Arm Encoder Position", m_absoluteEncoderPosition.getAsDouble());
    SmartDashboard.putNumber("SBSD Arm position error", m_controller.getPositionError());
    SmartDashboard.putBoolean("SBSD arm forward limit switch",
        m_rightAngleMotor.isForwardLimitSwitchOn());
    SmartDashboard.putBoolean("SBSD arm backward limit switch",
        m_rightAngleMotor.isReverseLimitSwitchOn());
  }

  @Override
  public boolean limitSwitchActive() {
    return m_rightAngleMotor.isForwardLimitSwitchOn() || m_rightAngleMotor.isReverseLimitSwitchOn();
  }

  @Override
  public void setCoastMode() {
    m_rightAngleMotor.setCoastMode();
    m_leftAngleMotor.setCoastMode();
  }

  @Override
  public void setBrakeMode() {
    m_rightAngleMotor.setBrakeMode();
    m_leftAngleMotor.setBrakeMode();
  }

  @Override
  public void setEndEffector(CoralEndEffectorRotateBase endEffector) {
    m_endEffector = endEffector;
  }

  @Override
  public void rotate(double speed) {
    MathUtil.clamp(speed, -m_maxSpeed, m_maxSpeed);
    if (!m_endEffector.isEndEffectorSafe() && (getAngleDeg() <= m_safetyAngle) && (speed < 0)) {
      stop();
    } else if ((getAngleDeg() <= m_minAngleDeg) && (speed < 0)) {
      stop();
    } else if ((getAngleDeg() >= m_maxAngleDeg) && (speed > 0)) {
      stop();
    } else {
      m_rightAngleMotor.setSpeed(speed);
      m_leftAngleMotor.setSpeed(speed);
    }
    SmartDashboard.putNumber("SBSD Arm Speed: ", speed);
  }

  @Override
  public void setGoalDeg(double goal) {
    m_controller.setSetpoint((goal) * Math.PI / 180);
    m_kG = SmartDashboard.getNumber("SBSD Arm kG", m_kG);
    m_kP = SmartDashboard.getNumber("SBSD Arm kP", m_kP);
    m_controller.setP(m_kP);
  }

  @Override
  public boolean atSetPoint() {
    return m_controller.atSetpoint();
  }

  public void calculateSpeed() {
    double currentAngleRad = getAngleRad();
    double pidCalc = m_controller.calculate(currentAngleRad);
    double feedforwardCalc = m_kG * Math.cos(getAngleRad());
    double speed = pidCalc + feedforwardCalc;
    rotate(speed);
    SmartDashboard.putNumber("SBSD Arm Error", m_controller.getPositionError());
    SmartDashboard.putNumber("SBSD Arm pidCalc", pidCalc);
    SmartDashboard.putNumber("SBSD Arm feedForwardCalc", feedforwardCalc);
    SmartDashboard.putNumber("SBSD Arm Current setpoint:", m_controller.getSetpoint());
  }

  @Override
  public void reloadConfig() {
    m_minAngleDeg = Config4905.getConfig4905().getSBSDArmConfig().getDouble("minAngleDeg");
    m_maxAngleDeg = Config4905.getConfig4905().getSBSDArmConfig().getDouble("maxAngleDeg");
    m_angleOffset = Config4905.getConfig4905().getSBSDArmConfig().getDouble("angleOffset");
    m_maxSpeed = Config4905.getConfig4905().getSBSDArmConfig().getDouble("maxSpeed");
    m_kP = Config4905.getConfig4905().getSBSDArmConfig().getDouble("kP");
    m_kI = Config4905.getConfig4905().getSBSDArmConfig().getDouble("kI");
    m_kD = Config4905.getConfig4905().getSBSDArmConfig().getDouble("kD");
    m_tolerance = Config4905.getConfig4905().getSBSDArmConfig().getDouble("tolerance");
  }
}
