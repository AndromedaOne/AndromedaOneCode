// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.samArmExtRet;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.actuators.HitecHS322HDpositionalServoMotor;
import frc.robot.actuators.SparkMaxController;

public class RealSamArmExtRet extends SamArmExtRetBase {

  private final SparkMaxController m_extensionMotor;
  private double m_zeroOffset = 0;
  private double m_maxExtension = 0;
  private double m_minExtension = 0;
  private boolean m_isInitialized = false;
  private DigitalInput m_retractLimitSwitch = new DigitalInput(9);
  private static final double m_armFrontMaxDistanceFromPivotPoint = 66;
  private static final double m_armRearMaxDistanceFromPivotPoint = 59;
  private static final double m_armLengthOffset = 37;
  private HitecHS322HDpositionalServoMotor m_extensionBrakeServoMotor;
  private double m_extensionBrakeOpenValue = 0.0;
  private double m_extensionBrakeClosedValue = 0.0;
  private ExtensionBrakeStates m_extensionBrakeStates = ExtensionBrakeStates.UNKNOWN;
  private double m_calcultedMaxExtendDistance = 0.0;

  /** Creates a new RealSamArmExtension. */
  public RealSamArmExtRet() {
    Config armextensionConfig = Config4905.getConfig4905().getSamArmExtensionConfig();

    m_extensionBrakeServoMotor = new HitecHS322HDpositionalServoMotor(armextensionConfig,
        "extensionbrake");
    m_extensionBrakeOpenValue = armextensionConfig.getDouble("extensionbrake.extensionbrakeopen");
    m_extensionBrakeClosedValue = armextensionConfig
        .getDouble("extensionbrake.extensionbrakeclosed");
    m_extensionMotor = new SparkMaxController(armextensionConfig, "extensionmotor");
    m_maxExtension = armextensionConfig.getDouble("maxExtension");
    m_minExtension = armextensionConfig.getDouble("minExtension");
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("arm extension position", getPosition());
    SmartDashboard.putNumber("arm extension ticks", getTicks());
    SmartDashboard.putString("arm ret limit switch", getRetractLimitSwitchState().toString());
    SmartDashboard.putString("Arm Extension brake state", getExtensionBrakeState().toString());
    SmartDashboard.putNumber("Calculated Max Extend Distance", calcMaxExtendDistance());
  }

  @Override
  public void extendRetract(double speed) {
    if (m_extensionBrakeStates != ExtensionBrakeStates.BRAKEOPEN) {
      return;
    }
    double maxExtDist = calcMaxExtendDistance();
    if ((speed < 0) && (getPosition() <= m_minExtension)) {
      m_extensionMotor.setSpeed(0);
    } else if ((speed > 0) && ((getPosition() >= m_maxExtension) || (getPosition() > maxExtDist))) {
      m_extensionMotor.setSpeed(0);
    } else {
      m_extensionMotor.setSpeed(speed);
    }
  }

  // 100 ticks = 1 inch
  @Override
  public double getPosition() {
    return (m_extensionMotor.getEncoderPositionTicks() - m_zeroOffset) / 100;
  }

  public double getTicks() {
    return m_extensionMotor.getEncoderPositionTicks();
  }

  @Override
  public void setZeroOffset() {
    m_zeroOffset = m_extensionMotor.getEncoderPositionTicks() + 25;
  }

  @Override
  public RetractLimitSwitchState getRetractLimitSwitchState() {
    return m_retractLimitSwitch.get() ? RetractLimitSwitchState.CLOSED
        : RetractLimitSwitchState.OPEN;
  }

  @Override
  public void retractArmInitialize() {
    if (m_extensionBrakeStates != ExtensionBrakeStates.BRAKEOPEN) {
      return;
    }
    if (getRetractLimitSwitchState() == RetractLimitSwitchState.CLOSED) {
      m_extensionMotor.setSpeed(0);
    } else {
      m_extensionMotor.setSpeed(-0.25);
    }
  }

  @Override
  public ExtensionBrakeStates getExtensionBrakeStates() {
    return m_extensionBrakeStates;
  }

  @Override
  public boolean isInitialized() {
    return m_isInitialized;
  }

  @Override
  public void setInitialized() {
    m_isInitialized = true;
  }

  private double calcMaxExtendDistance() {
    double angle = Robot.getInstance().getSubsystemsContainer().getArmRotateBase().getAngle();
    if (angle == 180) {
      return Double.MAX_VALUE;
    }
    double cosAngle = 0;
    if (angle < 180) {
      angle = (angle - 90);
      cosAngle = Math.cos(Math.toRadians(angle));
      m_calcultedMaxExtendDistance = m_armFrontMaxDistanceFromPivotPoint / cosAngle;
    } else {
      angle = Math.abs(angle - 270);
      cosAngle = Math.cos(Math.toRadians(angle));
      m_calcultedMaxExtendDistance = m_armRearMaxDistanceFromPivotPoint / cosAngle;
    }
    m_calcultedMaxExtendDistance -= m_armLengthOffset;
    return m_calcultedMaxExtendDistance;
  }

  @Override
  public void engageArmBrake() {
    m_extensionBrakeServoMotor.set(m_extensionBrakeClosedValue);
    m_extensionBrakeStates = ExtensionBrakeStates.BRAKECLOSED;
  }

  @Override
  public void disengageArmBrake() {
    m_extensionBrakeServoMotor.set(m_extensionBrakeOpenValue);
    m_extensionBrakeStates = ExtensionBrakeStates.BRAKEOPEN;
  }

  @Override
  public ExtensionBrakeStates getExtensionBrakeState() {
    return m_extensionBrakeStates;
  }

}
