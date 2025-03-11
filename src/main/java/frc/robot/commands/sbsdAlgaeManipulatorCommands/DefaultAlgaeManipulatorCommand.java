// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdAlgaeManipulatorCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdAlgaeManipulator.SBSDAlgaeManipulatorBase;
import frc.robot.telemetries.Trace;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class DefaultAlgaeManipulatorCommand extends Command {
  private SBSDAlgaeManipulatorBase m_sbsdAlgaeManipulatorBase;
  private Config m_algaeManipulatorConfig;
  private AlgaeManipulatorState m_currentState = AlgaeManipulatorState.STOW_POSITION;
  private POVButton m_pickupButton = Robot.getInstance().getOIContainer().getSubsystemController()
      .getPickupButtonAlgae();
  private POVButton m_scoreButton = Robot.getInstance().getOIContainer().getSubsystemController()
      .getScoreButtonAlgae();

  private enum AlgaeManipulatorState {
    INITIALIZE, STOW_POSITION, INTAKE_ALGAE, HOLD_ALGAE, SCORE_ALGAE
  }

  public DefaultAlgaeManipulatorCommand() {
    m_algaeManipulatorConfig = Config4905.getConfig4905().getSBSDAlgaeManipulatorConfig();
    m_sbsdAlgaeManipulatorBase = Robot.getInstance().getSubsystemsContainer()
        .getSBSDAlgaeManipulatorBase();
    addRequirements(m_sbsdAlgaeManipulatorBase.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_currentState = AlgaeManipulatorState.INITIALIZE;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (m_currentState) {
    case INITIALIZE:
      Trace.getInstance().logCommandInfo(this, "Algae Manip State -> INITIALIZE");
      if (!m_sbsdAlgaeManipulatorBase.getAlgaeManipMaxAngleLimitSwitchState()) {
        m_sbsdAlgaeManipulatorBase
            .rotateForInitialize(m_algaeManipulatorConfig.getDouble("initiaizeSpeed"));
      } else {
        m_sbsdAlgaeManipulatorBase.resetAlgaeManipulatorAngle();
        Trace.getInstance().logCommandInfo(this, "Algae Manip State INIT-> STOW");
        m_currentState = AlgaeManipulatorState.STOW_POSITION;
        m_sbsdAlgaeManipulatorBase.setInitialized();
      }

    case STOW_POSITION:
      m_sbsdAlgaeManipulatorBase.stopAlgaeManipulatorIntakeWheels();
      m_sbsdAlgaeManipulatorBase
          .setAlgaeManipulatorAngleSetpoint(m_algaeManipulatorConfig.getDouble("stowAngle"));
      if (m_pickupButton.getAsBoolean()) {
        Trace.getInstance().logCommandInfo(this, "Algae Manip State Stow to  -> INTAKE");
        m_currentState = AlgaeManipulatorState.INTAKE_ALGAE;
      }
      break;

    case INTAKE_ALGAE:

      m_sbsdAlgaeManipulatorBase.runWheelsToIntake();
      m_sbsdAlgaeManipulatorBase
          .setAlgaeManipulatorAngleSetpoint(m_algaeManipulatorConfig.getDouble("intakeAngle"));
      if (!m_pickupButton.getAsBoolean()) {
        Trace.getInstance().logCommandInfo(this, "Algae Manip State INTAKE -> HOLD");
        m_currentState = AlgaeManipulatorState.HOLD_ALGAE;
      }
      break;

    case HOLD_ALGAE:
      m_sbsdAlgaeManipulatorBase.runWheelsToIntake();
      m_sbsdAlgaeManipulatorBase
          .setAlgaeManipulatorAngleSetpoint(m_algaeManipulatorConfig.getDouble("holdAngle"));
      if (m_scoreButton.getAsBoolean()) {
        Trace.getInstance().logCommandInfo(this, "Algae Manip State HOLD -> SCORE");
        m_currentState = AlgaeManipulatorState.SCORE_ALGAE;
      }
      break;

    case SCORE_ALGAE:
      m_sbsdAlgaeManipulatorBase
          .setAlgaeManipulatorAngleSetpoint(m_algaeManipulatorConfig.getDouble("scoreAngle"));
      if (m_sbsdAlgaeManipulatorBase.isAlgaeManipulatorOnTarget()) {
        m_sbsdAlgaeManipulatorBase.runWheelsToEject();
      }
      if (!m_scoreButton.getAsBoolean()) {
        Trace.getInstance().logCommandInfo(this, "Algae Manip State SCORE -> STOW");
        m_currentState = AlgaeManipulatorState.STOW_POSITION;
      }
      break;
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
