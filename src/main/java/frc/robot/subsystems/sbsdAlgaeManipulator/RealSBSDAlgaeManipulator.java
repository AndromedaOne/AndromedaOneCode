// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.sbsdAlgaeManipulator;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.pidcontroller.PIDController4905;

/** Add your docs here. */
public class RealSBSDAlgaeManipulator extends SubsystemBase implements SBSDAlgaeManipulatorBase {
  private SparkMaxController m_intakeWheels;
  private SparkMaxController m_deployAlgaeManipulator;
  private double m_intakeWheelSpeed = 0.0;
  private double m_ejectWheelSpeed = 0.0;
  private double m_deploySpeed = 0.0;
  private double m_retractSpeed = 0.0;
  private double m_deployAngle = 0.0;
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
    m_deploySpeed = algaeManipulatorConfig.getDouble("deploySpeed");
    m_retractSpeed = algaeManipulatorConfig.getDouble("retractSpeed");
    m_deployAngle = algaeManipulatorConfig.getDouble("deployAngle");
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
    m_intakeWheels.setSpeed(m_ejectWheelSpeed);
  }

  private double calculateCorrectedEncoderPosition() {
    double encoderPosition = (m_deployAlgaeManipulator.getBuiltInEncoderPositionTicks() * 360);
    return encoderPosition;
  }

  public void calculateDeploySpeed() {
  }

  @Override
  public void deployAlgaeManipulator() {
  }

  @Override
  public void retractAlgaeManipulator() {
  }

  @Override
  public void stopAlgaeManipulatorIntakeWheels() {
    m_intakeWheels.setSpeed(0);
  }
}
