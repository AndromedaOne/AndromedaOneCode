// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.topGunIntake;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.TalonSRXController;

public class RealIntake extends IntakeBase {
  private TalonSRXController m_wheelsController;
  private TalonSRXController m_deployRetractControllerDriverSide;
  private TalonSRXController m_deployRetractControllerPassengerSide;
  private Config m_intakeConfig = Config4905.getConfig4905().getIntakeConfig();

  /** Creates a new RealIntake. */
  public RealIntake() {
    m_wheelsController = new TalonSRXController(m_intakeConfig, "wheelsController");
    m_deployRetractControllerDriverSide = new TalonSRXController(m_intakeConfig,
        "deployRetractControllerDriverSide");
    m_deployRetractControllerPassengerSide = new TalonSRXController(m_intakeConfig,
        "deployRetractControllerPassengerSide");
  }

  public void runIntakeWheels(double speed) {
    // Run intake motors to bring game piece (ball) into robot
    // Or to push Ball out of Robot
    m_wheelsController.set(speed);
  }

  public void stopIntakeWheels() {
    // Stop intake motors
    m_wheelsController.stopMotor();
  }

  @Override
  public void deployIntake() {
    m_deployRetractControllerDriverSide.set(m_intakeConfig.getDouble("deploySpeed"));
    m_deployRetractControllerPassengerSide.set(m_intakeConfig.getDouble("deploySpeed"));
  }

  @Override
  public void retractIntake() {
    m_deployRetractControllerDriverSide.set(m_intakeConfig.getDouble("retractSpeed"));
    m_deployRetractControllerPassengerSide.set(m_intakeConfig.getDouble("retractSpeed"));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("IntakeDriverSideLimitSwitchFwdClosed",
        m_deployRetractControllerDriverSide.isFwdLimitSwitchClosed() == 1);
    SmartDashboard.putBoolean("IntakeDriverSideLimitSwitchRevClosed",
        m_deployRetractControllerDriverSide.isRevLimitSwitchClosed() == 1);
    SmartDashboard.putBoolean("IntakePassengerSideLimitSwitchFwdClosed",
        m_deployRetractControllerPassengerSide.isFwdLimitSwitchClosed() == 1);
    SmartDashboard.putBoolean("IntakePassengerSideLimitSwitchRevClosed",
        m_deployRetractControllerPassengerSide.isRevLimitSwitchClosed() == 1);
  }
}