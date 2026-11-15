// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.FuelRaiderCommands;

import java.time.Instant;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.armhopperintake.AHIBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class WiggleAHI extends Command {
  /** Creates a new BackAndForthAHI. */
  private Config m_ahiConfig = Config4905.getConfig4905().getAHIConfig();
  private AHIBase m_ahi = Robot.getInstance().getSubsystemsContainer().getAHI();
  private double m_angleOffset = m_ahiConfig.getDouble("offset");
  private double m_retractArmAngle = m_ahiConfig.getDouble("retractArm") - m_angleOffset - 15;
  private double m_wiggleAngleDelta = 50;
  private long m_timeoutDurationInMilliSeconds = 500;
  private Instant m_endTime;
  private SubsystemController m_controller;
  private boolean m_isTeleop;

  private enum ArmState {
    RETRACT, WIGGLE
  };

  private ArmState m_armState = ArmState.RETRACT;

  public WiggleAHI() {
    addRequirements(m_ahi.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_controller = Robot.getInstance().getOIContainer().getSubsystemController();
    m_isTeleop = !Robot.getInstance().isAutonomous();
    if (m_controller.eitherTriggerPressed() || !m_isTeleop) {
      m_ahi.setArmSetpoint(m_retractArmAngle);
    }

    m_endTime = Instant.now().plusMillis(m_timeoutDurationInMilliSeconds);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_controller.eitherTriggerPressed() || !m_isTeleop) {
      switch (m_armState) {
      case RETRACT:
        m_ahi.setArmSetpoint(m_retractArmAngle);
        if (m_ahi.atSetpoint() || (m_endTime.compareTo(Instant.now()) <= 0)) {
          m_armState = ArmState.WIGGLE;
          m_endTime = Instant.now().plusMillis(m_timeoutDurationInMilliSeconds);
        }
        break;
      case WIGGLE:
        m_ahi.setArmSetpoint(m_retractArmAngle - m_wiggleAngleDelta);
        if (m_ahi.atSetpoint() || (m_endTime.compareTo(Instant.now()) <= 0)) {
          m_armState = ArmState.RETRACT;
          m_endTime = Instant.now().plusMillis(m_timeoutDurationInMilliSeconds);
        }
        break;
      default:
        m_armState = ArmState.RETRACT;
        break;
      }
    } else {
      m_ahi.setArmSetpoint(m_ahiConfig.getDouble("extendArm") - 5);
    }
    m_ahi.rotateArmPID();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_ahi.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
