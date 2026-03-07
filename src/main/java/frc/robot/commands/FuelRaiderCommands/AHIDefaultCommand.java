// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.FuelRaiderCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.armhopperintake.AHIBase;
import frc.robot.subsystems.armhopperintake.RealAHI.State;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class AHIDefaultCommand extends Command {
  /** Creates a new AHIDefaultCommand. */
  private AHIBase m_ahi = Robot.getInstance().getSubsystemsContainer().getAHI();
  private State m_currentState = State.RETRACTEDSTART;
  private double m_retractArmAngle = 0.0;
  private double m_extendArmAngle = 0.0;
  private Config m_ahiConfig;
  private SubsystemController m_subsystemController;
  private boolean m_isPressed = false;

  public AHIDefaultCommand() {
    m_ahiConfig = Config4905.getConfig4905().getAHIConfig();
    double offset = m_ahiConfig.getDouble("offset");
    m_retractArmAngle = m_ahiConfig.getDouble("retractArm") - offset;
    m_extendArmAngle = m_ahiConfig.getDouble("extendArm") - offset;
    m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
    addRequirements(m_ahi.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_ahi.setArmSetpoint(m_retractArmAngle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean usePID = true;
    if (m_ahi.isRetracting()) {
      m_currentState = State.RETRACTEDSTART;
      m_ahi.setState(m_currentState);
    } else if (m_ahi.isExtending()) {
      m_currentState = State.EXTENDEDSTART;
      m_ahi.setState(m_currentState);
    } else if ((m_subsystemController.getAButtonPressed()) && !m_isPressed) {
      m_isPressed = true;
      if (m_currentState == State.EXTENDED || m_currentState == State.HOLDEXTENDED) {
        m_currentState = State.RETRACTEDSTART;
        m_ahi.setState(m_currentState);
      } else if (m_currentState == State.RETRACTED) {
        m_currentState = State.EXTENDEDSTART;
        m_ahi.setState(m_currentState);
      }
    } else if (m_subsystemController.getAButtonPressed()) {
      if (m_currentState == State.EXTENDEDSTART) {
        m_currentState = State.EXTENDED;
        m_ahi.setState(m_currentState);
      } else if (m_currentState == State.RETRACTEDSTART) {
        m_currentState = State.RETRACTED;
        m_ahi.setState(m_currentState);
      } else if (m_ahi.atSetpoint() && m_currentState == State.EXTENDED) {
        m_currentState = State.HOLDEXTENDED;
        m_ahi.setState(m_currentState);
      }
    } else {
      m_isPressed = false;
      if (m_currentState == State.EXTENDEDSTART) {
        m_currentState = State.EXTENDED;
        m_ahi.setState(m_currentState);
      } else if (m_currentState == State.RETRACTEDSTART) {
        m_currentState = State.RETRACTED;
        m_ahi.setState(m_currentState);
      } else if (m_ahi.atSetpoint() && m_currentState == State.EXTENDED) {
        m_currentState = State.HOLDEXTENDED;
        m_ahi.setState(m_currentState);
      }
    }
    switch (m_currentState) {
    case RETRACTED:
      break;
    case EXTENDED:
      break;
    case HOLDEXTENDED:
      m_ahi.rotateArm(-0.1, false);
      usePID = false;
      break;
    case RETRACTEDSTART:
      m_ahi.setArmSetpoint(m_retractArmAngle);
      break;
    case EXTENDEDSTART:
      m_ahi.setArmSetpoint(m_extendArmAngle);
      break;
    default:
      break;

    }
    // later on, we might need to have separate move and hold states.
    // TODO: do we set brake mode or keep PID'ing?
    if (usePID) {
      m_ahi.rotateArmPID();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
