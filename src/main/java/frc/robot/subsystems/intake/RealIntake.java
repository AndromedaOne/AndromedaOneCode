// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealIntake extends IntakeBase {
  private SparkMaxController m_wheelsController;
  private SparkMaxController m_deployRetractController;
  private Config m_intakeConfig = Config4905.getConfig4905().getIntakeConfig();

  /** Creates a new RealIntake. */
  public RealIntake() {
    m_wheelsController = new SparkMaxController(m_intakeConfig, "wheelsController");
    m_deployRetractController = new SparkMaxController(m_intakeConfig, "deployRetractController");
  }

  public void runIntake(double speed) {
    // Run intake motors to bring game piece (ball) into robot
    m_wheelsController.set(speed);
  }

  public void stopIntake() {
    // Stop intake motors
    m_wheelsController.stopMotor();
  }

  @Override
  public void deployIntake() {
    m_deployRetractController.set(m_intakeConfig.getDouble("deploySpeed"));
    System.out.println("Deploying Intake");
  }

  @Override
  public void retractIntake() {
    m_deployRetractController.set(m_intakeConfig.getDouble("retractSpeed"));
  }
}