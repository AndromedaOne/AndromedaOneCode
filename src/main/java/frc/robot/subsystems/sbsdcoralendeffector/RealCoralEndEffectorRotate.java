// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdcoralendeffector;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.sbsdclimber.ClimberMode;

/** Add your docs here. */
public class RealCoralEndEffectorRotate extends SubsystemBase
    implements CoralEndEffectorRotateBase {

  private SparkMaxController m_angleMotor;
  private DoubleSupplier m_absoluteEncoderPosition;
  private double m_minAngleDeg = 0.0;
  private double m_maxAngleDeg = 0.0;
  private double m_angleOffset = 0.0;
  private double m_maxSpeed = 0.0;
  private double m_kP = 0.0;
  private double m_kI = 0.0;
  private double m_kD = 0.0;
  private double m_kG = 0.0;
  private double m_maxSpeedCloseToMax = 0.0;
  private double m_closeToMaxAngle = 0.0;
  private double m_safeAngle = 0.0;
  private double m_tolerance = 0.0;
  private static boolean m_inClimberMode = false;
  private PIDController4905 m_controller;

  public RealCoralEndEffectorRotate() {
    Config config = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig();
    Config sensorConfig = Config4905.getConfig4905().getSensorConfig();
    m_angleMotor = new SparkMaxController(config, "coralAngle", false, false);
    m_absoluteEncoderPosition = () -> m_angleMotor.getAbsoluteEncoderPosition();
    m_tolerance = config.getDouble("tolerance");
    m_kP = config.getDouble("kP");
    m_kI = config.getDouble("kI");
    m_kD = config.getDouble("kD");
    m_kG = config.getDouble("kG");
    m_angleOffset = config.getDouble("angleOffset"); // This is in rotations
    m_minAngleDeg = config.getDouble("minAngleDeg");
    m_maxAngleDeg = config.getDouble("maxAngleDeg");
    m_maxSpeed = config.getDouble("maxSpeed");
    m_maxSpeedCloseToMax = config.getDouble("maxSpeedCloseToMax");
    m_closeToMaxAngle = config.getDouble("closeToMaxAngleDeg");
    m_safeAngle = config.getDouble("safeAngleDeg");
    m_controller = new PIDController4905("Coral PID", m_kP, m_kI, m_kD, 0);
    SmartDashboard.putNumber("Coral kG", m_kG);
    SmartDashboard.putNumber("Coral kP", m_kP);
    m_controller.disableContinuousInput();
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
  public void setAngleDeg(double angle) {
    m_controller.setSetpoint((angle) * Math.PI / 180);
    m_kG = SmartDashboard.getNumber("Coral kG", m_kG);
    m_kP = SmartDashboard.getNumber("Coral kP", m_kP);
    m_controller.setP(m_kP);
  }

  private double calculateCorrectedEncoder() {
    double correctedEncoderValue = m_absoluteEncoderPosition.getAsDouble();
    if (correctedEncoderValue >= m_angleOffset) {
      correctedEncoderValue = correctedEncoderValue - m_angleOffset;
    } else {
      correctedEncoderValue = correctedEncoderValue + (1 - m_angleOffset);
    }
    if (correctedEncoderValue > 0.5) {
      correctedEncoderValue = correctedEncoderValue - 1;
    }
    SmartDashboard.putNumber("Coral Corrected Encoder Value", correctedEncoderValue);
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
  public void setCoastMode() {
    m_angleMotor.setCoastMode();
  }

  @Override
  public void setBrakeMode() {
    m_angleMotor.setBrakeMode();
  }

  @Override
  public boolean isEndEffectorSafe() {
    if (getAngleDeg() >= m_safeAngle) {
      return true;
    }
    return false;
  }

  @Override
  public void stop() {
    m_angleMotor.setSpeed(0);
  }

  @Override
  public void rotate(double speed) {
    if (ClimberMode.getInstance().getInClimberMode()) {
      stop();
    } else {
      if (getAngleDeg() >= m_closeToMaxAngle) {
        speed = MathUtil.clamp(speed, -m_maxSpeedCloseToMax, m_maxSpeedCloseToMax);
      } else {
        speed = MathUtil.clamp(speed, -m_maxSpeed, m_maxSpeed);
      }
      if ((getAngleDeg() <= m_minAngleDeg) && (speed < 0)) {
        stop();
      } else if ((getAngleDeg() >= m_maxAngleDeg) && (speed > 0)) {
        stop();
      } else {
        m_angleMotor.setSpeed(speed);
      }
      SmartDashboard.putNumber("Coral Speed: ", speed);
    }

  }

  @Override
  public void reloadConfig() {
    m_minAngleDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
        .getDouble("minAngleDeg");
    m_maxAngleDeg = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
        .getDouble("maxAngleDeg");
    m_angleOffset = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
        .getDouble("angleOffset");
    m_maxSpeed = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("maxSpeed");
    m_kP = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("kP");
    m_kI = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("kI");
    m_kD = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("kD");
    m_kG = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("kG");
    m_maxSpeedCloseToMax = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
        .getDouble("maxSpeedCloseToMax");
    m_closeToMaxAngle = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig()
        .getDouble("closeToMaxAngleDeg");
    m_tolerance = Config4905.getConfig4905().getSBSDCoralEndEffectorConfig().getDouble("tolerance");
  }

  @Override
  public void calculateSpeed() {
    double currentAngleRad = getAngleRad();
    double pidCalc = m_controller.calculate(currentAngleRad);
    double feedforwardCalc = m_kG * Math.cos(getAngleRad());
    double speed = pidCalc + feedforwardCalc;
    rotate(speed);
    SmartDashboard.putNumber("Coral Error", m_controller.getPositionError());
    SmartDashboard.putNumber("Coral pidCalc", pidCalc);
    SmartDashboard.putNumber("Coral feedForwardCalc", feedforwardCalc);
    SmartDashboard.putNumber("Coral Current setpoint:", m_controller.getSetpoint());
  }

  @Override
  public boolean atSetPoint() {
    return m_controller.atSetpoint();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Coral Angle in Degrees", getAngleDeg());
    SmartDashboard.putNumber("Coral Angle in Rads", getAngleRad());
    SmartDashboard.putNumber("Coral Angle Encoder Position",
        m_absoluteEncoderPosition.getAsDouble());
    SmartDashboard.putNumber("Coral Angle position error", m_controller.getPositionError());
  }
}
