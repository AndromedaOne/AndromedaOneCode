// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdAlgaeManipulator;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.sensors.limitswitchsensor.RealLimitSwitchSensor;

/** Add your docs here. */
public class RealSBSDAlgaeManipulator extends SubsystemBase implements SBSDAlgaeManipulatorBase {
  private SparkMaxController m_intakeWheels;
  private SparkMaxController m_deployAlgaeManipulator;
  private RealLimitSwitchSensor m_algaeManipMaxAngleLimitSwitch;
  private double m_intakeWheelSpeed = 0.0;
  private double m_ejectWheelSpeed = 0.0;
  private double m_deployAngle = 0.0;
  private double m_retractAngle = 0.0;
  private double m_kP = 0.0;
  private double m_kI = 0.0;
  private double m_kD = 0.0;
  private boolean m_isInitialized = false;
  private PIDController4905 m_pidController = new PIDController4905("AlgaeManipulatorPID");

  public RealSBSDAlgaeManipulator() {
    Config algaeManipulatorConfig = Config4905.getConfig4905().getSBSDAlgaeManipulatorConfig();
    m_intakeWheels = new SparkMaxController(algaeManipulatorConfig, "intakeWheels", false, false);
    m_deployAlgaeManipulator = new SparkMaxController(algaeManipulatorConfig, "pickupMotor", false,
        false);
    m_algaeManipMaxAngleLimitSwitch = new RealLimitSwitchSensor(
        "algaeManipMaxAngleLimitSwitchPort");
    m_intakeWheelSpeed = algaeManipulatorConfig.getDouble("intakeWheelSpeed");
    m_ejectWheelSpeed = algaeManipulatorConfig.getDouble("ejectWheelSpeed");
    m_deployAngle = algaeManipulatorConfig.getDouble("intakeAngle");
    m_retractAngle = algaeManipulatorConfig.getDouble("stowAngle");
    m_kP = algaeManipulatorConfig.getDouble("kP");
    m_kI = algaeManipulatorConfig.getDouble("kI");
    m_kD = algaeManipulatorConfig.getDouble("kD");
    m_pidController.setPID(m_kP, m_kI, m_kD);
    m_pidController.setSetpoint(0);
    m_pidController.setTolerance(algaeManipulatorConfig.getInt("tolerance"));
    m_pidController.setMaxOutput(algaeManipulatorConfig.getDouble("angleMaxSpeed"));
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
  public void runWheelsToIntake() {
    m_intakeWheels.setSpeed(m_intakeWheelSpeed);
  }

  @Override
  public void runWheelsToEject() {
    m_intakeWheels.setSpeed(-m_ejectWheelSpeed);
  }

  @Override
  public void stopAlgaeManipulatorIntakeWheels() {
    m_intakeWheels.setSpeed(0);
  }

  private double getEncoderPositionInDegrees() {
    double encoderPosition = (m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks() * 360);
    return encoderPosition;
  }

  private double getEncoderPositionInRadians() {
    double encoderPosition = (m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks() * 2
        * Math.PI);
    return encoderPosition;
  }

  private void rotateWithPID(double speed) {
    if (((m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks() > m_retractAngle)
        || m_algaeManipMaxAngleLimitSwitch.isAtLimit()) && (speed > 0)) {
      speed = 0.0;
    } else if ((m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks() < m_deployAngle)
        && (speed < 0)) {
      speed = 0.0;
    }
    SmartDashboard.putNumber("Algae Manip Speed", speed);
    m_deployAlgaeManipulator.setSpeed(speed);
  }

  private void calculateSpeed() {
    if (m_isInitialized) {
      double pidCalc = m_pidController
          .calculate(m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks());
      rotateWithPID(pidCalc);
    }
  }

  @Override
  public void moveUsingSmartDashboard(double speed) {
    rotateWithPID(speed);
  }

  @Override
  public boolean isAlgaeManipulatorOnTarget() {
    return m_pidController.atSetpoint();
  }

  @Override
  public void reloadConfig() {
    Config algaeManipulatorConfig = Config4905.getConfig4905().getSBSDAlgaeManipulatorConfig();
    m_intakeWheelSpeed = algaeManipulatorConfig.getDouble("intakeWheelSpeed");
    m_ejectWheelSpeed = algaeManipulatorConfig.getDouble("ejectWheelSpeed");
    m_deployAngle = algaeManipulatorConfig.getDouble("deployAngle");
    m_retractAngle = algaeManipulatorConfig.getDouble("retractAngle");
    m_kP = algaeManipulatorConfig.getDouble("kP");
    m_kI = algaeManipulatorConfig.getDouble("kI");
    m_kD = algaeManipulatorConfig.getDouble("kD");
    m_pidController.setPID(m_kP, m_kI, m_kD);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Algae Manip Encoder Ticks",
        m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks());
    SmartDashboard.putBoolean("Algae Manip Max Angle Limit Switch",
        m_algaeManipMaxAngleLimitSwitch.isAtLimit());
    calculateSpeed();
  }

  @Override
  public void resetAlgaeManipulatorAngle() {
    m_deployAlgaeManipulator.resetEncoder();
    m_pidController.reset();
  }

  @Override
  public void setAlgaeManipulatorAngleSetpoint(double angle) {
    m_pidController.setSetpoint(angle);
  }

  @Override
  public void initializeSpeed() {
  }

  @Override
  public void rotateForInitialize(double speed) {
    if (!getAlgaeManipMaxAngleLimitSwitchState()) {
      m_deployAlgaeManipulator.setSpeed(speed);
    } else {
      m_deployAlgaeManipulator.setSpeed(0);
    }
  }

  @Override
  public void setInitialized() {
    m_isInitialized = true;
  }

  @Override
  public boolean getAlgaeManipMaxAngleLimitSwitchState() {
    return m_algaeManipMaxAngleLimitSwitch.isAtLimit();
  }
}
