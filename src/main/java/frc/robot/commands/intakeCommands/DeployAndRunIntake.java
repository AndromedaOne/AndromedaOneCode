// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.intakeCommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.telemetries.Trace;

public class DeployAndRunIntake extends CommandBase {
  /** Creates a new DeployAndRunIntake. */
  private IntakeBase m_intakeBase;
  private Config m_intakeConfig = Config4905.getConfig4905().getIntakeConfig();
  private double m_intakeSpeed = 0;
  private boolean m_runInReverse = false;

  public DeployAndRunIntake(IntakeBase intakeBase, boolean runInReverse) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeBase);
    m_intakeBase = intakeBase;
    m_intakeSpeed = m_intakeConfig.getDouble("intakeSpeed");
    m_runInReverse = runInReverse;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    m_intakeBase.deployIntake();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double intakeSpeed = m_intakeSpeed;
    if (m_runInReverse) {
      intakeSpeed *= -1;
    }
    m_intakeBase.runIntake(intakeSpeed);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intakeBase.stopIntake();
    m_intakeBase.retractIntake();
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean finished = Robot.getInstance().getOIContainer().getSubsystemController()
        .getRunIntakeButtonReleased();
    if (m_runInReverse) {
      finished = Robot.getInstance().getOIContainer().getSubsystemController()
          .getRunIntakeinReverseButtonReleased();
    }
    return finished;
  }
}
