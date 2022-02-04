// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.TalonSRXController;

public class RealIntake extends IntakeBase {
  private TalonSRXController m_intakeController;
  private DoubleSolenoid4905 m_intakeDeploymentSolenoid;

  /** Creates a new RealIntake. */
  public RealIntake() {
    Config intakeConfig = Config4905.getConfig4905().getIntakeConfig();
    m_intakeController = new TalonSRXController(intakeConfig, "IntakeSRXController");
    m_intakeDeploymentSolenoid = new DoubleSolenoid4905(intakeConfig, "IntakeSolonoid");
  }

  public void runIntake(double speed) {
    // Run intake motors to bring game piece (ball) into robot
    m_intakeController.set(speed);
  }

  public void stopIntake() {
    // Stop intake motors
    m_intakeController.stopMotor();
  }

  @Override
  public void deployIntake() {
    m_intakeDeploymentSolenoid.extendPiston();
    System.out.println("Deploying Intake");
  }

  @Override
  public void retractIntake() {
    m_intakeDeploymentSolenoid.retractPiston();
  }
}