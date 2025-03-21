// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdArmCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdcoralendeffector.CoralEndEffectorRotateBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class InitializeEERotate extends Command {
  private CoralEndEffectorRotateBase m_endEffector;
  private boolean m_eeAngleFixerInitialState = false;

  public InitializeEERotate() {
    m_endEffector = Robot.getInstance().getSubsystemsContainer()
        .getSBSDCoralEndEffectorRotateBase();
    addRequirements(m_endEffector.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_eeAngleFixerInitialState = m_endEffector.getEEAngleFixerSensor();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_eeAngleFixerInitialState) {
      if (!m_eeAngleFixerInitialState) {
        // found edge
        m_endEffector.setAngleOffset();
        m_endEffector.stop();
        m_endEffector.setInitialized();
      } else {
        m_endEffector.rotate(0.01);
      }
    } else {
      if (m_eeAngleFixerInitialState) {
        // found edge
        m_endEffector.setAngleOffset();
        m_endEffector.stop();
        m_endEffector.setInitialized();
      } else {
        m_endEffector.rotate(-0.01);
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_endEffector.isInitialized();
  }
}
