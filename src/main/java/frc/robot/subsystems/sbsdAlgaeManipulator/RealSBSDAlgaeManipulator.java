// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdAlgaeManipulator;

import com.typesafe.config.Config;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class RealSBSDAlgaeManipulator extends SubsystemBase implements SBSDAlgaeManipulatorBase {
  private SparkMaxController m_intakeWheels;
  private SparkMaxController m_deployAlgaeManipulator;
  private double m_intakeWheelSpeed = 0.0;
  private double m_ejectWheelSpeed = 0.0;
  private double m_maxDeploySpeed = 0.0;
  private double m_maxRetractSpeed = 0.0;
  private double m_deployAngle = 0.0;
  private double m_retractAngle = 0.0;
  private double m_kP = 0.0;
  private double m_kI = 0.0;
  private double m_kD = 0.0;
  private PIDController4905 m_pidController = new PIDController4905("AlgaeManipulatorPID");

  public RealSBSDAlgaeManipulator() {
    Config algaeManipulatorConfig = Config4905.getConfig4905().getSBSDAlgaeManipulatorConfig();
    m_intakeWheels = new SparkMaxController(algaeManipulatorConfig, "intakeWheels", false, false);
    m_deployAlgaeManipulator = new SparkMaxController(algaeManipulatorConfig, "pickupMotor", false,
        false);
    m_intakeWheelSpeed = algaeManipulatorConfig.getDouble("intakeWheelSpeed");
    m_ejectWheelSpeed = algaeManipulatorConfig.getDouble("ejectWheelSpeed");
    m_maxDeploySpeed = algaeManipulatorConfig.getDouble("deploySpeed");
    m_maxRetractSpeed = algaeManipulatorConfig.getDouble("retractSpeed");
    m_deployAngle = algaeManipulatorConfig.getDouble("deployAngle");
    m_retractAngle = algaeManipulatorConfig.getDouble("retractAngle");
    m_kP = algaeManipulatorConfig.getDouble("kP");
    m_kI = algaeManipulatorConfig.getDouble("kI");
    m_kD = algaeManipulatorConfig.getDouble("kD");
    m_pidController.setPID(m_kP, m_kI, m_kD);
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

  private void calcSpeed(double speed) {
    SmartDashboard.putNumber("SBSD Unmodified Algae Speed: ", speed);
    speed = MathUtil.clamp(speed, -m_maxRetractSpeed, m_maxDeploySpeed);
    // the polarities might be wrong
    if ((getEncoderPositionInDegrees() > m_deployAngle) && (speed > 0)) {
      speed = 0.0;
    } else if ((getEncoderPositionInDegrees() < m_retractAngle) && (speed < 0)) {
      speed = 0.0;
    }
    m_deployAlgaeManipulator.setSpeed(speed);
    SmartDashboard.putNumber("SBSD Algae Speed: ", speed);
  }

  @Override
  public void moveAlgaeManipulatorUsingPID() {
    double currentAngleRad = getEncoderPositionInRadians();
    double pidCalc = m_pidController.calculate(currentAngleRad);
    calcSpeed(pidCalc);
  }

  @Override
  public void moveUsingSmartDashboard(double speed) {
    calcSpeed(speed);
  }

  @Override
  public void setDeploySetpoint() {
    m_pidController.setSetpoint(m_deployAngle);
    Trace.getInstance().logInfo("Set algae setpoint to " + m_deployAngle + ", deploy angle");
  }

  @Override
  public void setRetractSetpoint() {
    m_pidController.setSetpoint(m_retractAngle);
    Trace.getInstance().logInfo("Set algae setpoint to " + m_retractAngle + ", retract angle");
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
    m_maxDeploySpeed = algaeManipulatorConfig.getDouble("deploySpeed");
    m_maxRetractSpeed = algaeManipulatorConfig.getDouble("retractSpeed");
    m_deployAngle = algaeManipulatorConfig.getDouble("deployAngle");
    m_retractAngle = algaeManipulatorConfig.getDouble("retractAngle");
    m_kP = algaeManipulatorConfig.getDouble("kP");
    m_kI = algaeManipulatorConfig.getDouble("kI");
    m_kD = algaeManipulatorConfig.getDouble("kD");
    m_pidController.setPID(m_kP, m_kI, m_kD);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("SBSD Algae Angle in Degrees", getEncoderPositionInDegrees());
    SmartDashboard.putNumber("SBSD Algae Angle in Rads", getEncoderPositionInRadians());
    SmartDashboard.putNumber("SBSD Algae Encoder Position",
        m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks());
    SmartDashboard.putNumber("SBSD Algae position error", m_pidController.getPositionError());
    SmartDashboard.putBoolean("SBSD Algae On Target", isAlgaeManipulatorOnTarget());
  }
}
