// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdAlgaeManipulatorCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdAlgaeManipulator.SBSDAlgaeManipulatorBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class DefaultAlgaeManipulatorCommand extends Command {
  private SBSDAlgaeManipulatorBase m_sbsdAlgaeManipulatorBase;
  private AlgaeManipulatorState m_currentState = AlgaeManipulatorState.DEFAULT_POSITION;
  private POVButton m_pickupButton = Robot.getInstance().getOIContainer().getSubsystemController()
      .getPickupButtonAlgae();
  private POVButton m_scoreButton = Robot.getInstance().getOIContainer().getSubsystemController()
      .getScoreButtonAlgae();

  private enum AlgaeManipulatorState {
    DEFAULT_POSITION, INTAKE_ALGAE, HOLD_ALGAE, SCORE_ALGAE
  }

  public DefaultAlgaeManipulatorCommand() {
    m_sbsdAlgaeManipulatorBase = Robot.getInstance().getSubsystemsContainer()
        .getSBSDAlgaeManipulatorBase();
    addRequirements(m_sbsdAlgaeManipulatorBase.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_sbsdAlgaeManipulatorBase.setRetractSetpoint();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (m_currentState) {
    case DEFAULT_POSITION:
      m_sbsdAlgaeManipulatorBase.moveAlgaeManipulatorUsingPID();
      m_sbsdAlgaeManipulatorBase.stopAlgaeManipulatorIntakeWheels();
      if (m_pickupButton.getAsBoolean()) {
        m_sbsdAlgaeManipulatorBase.setDeploySetpoint();
        m_currentState = AlgaeManipulatorState.INTAKE_ALGAE;
      }
      break;
    case INTAKE_ALGAE:
      m_sbsdAlgaeManipulatorBase.moveAlgaeManipulatorUsingPID();
      m_sbsdAlgaeManipulatorBase.runWheelsToIntake();
      if (!m_pickupButton.getAsBoolean()) {
        m_sbsdAlgaeManipulatorBase.setRetractSetpoint();
        m_currentState = AlgaeManipulatorState.HOLD_ALGAE;
      }
      break;
    case HOLD_ALGAE:
      m_sbsdAlgaeManipulatorBase.moveAlgaeManipulatorUsingPID();
      m_sbsdAlgaeManipulatorBase.runWheelsToIntake();
      if (m_scoreButton.getAsBoolean()) {
        m_currentState = AlgaeManipulatorState.SCORE_ALGAE;
      }
      break;
    case SCORE_ALGAE:
      m_sbsdAlgaeManipulatorBase.moveAlgaeManipulatorUsingPID();
      m_sbsdAlgaeManipulatorBase.runWheelsToEject();
      if (!m_scoreButton.getAsBoolean()) {
        m_currentState = AlgaeManipulatorState.DEFAULT_POSITION;
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
