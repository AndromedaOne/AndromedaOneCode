/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.telemetries.Trace;

public class DeployAndRunIntake extends CommandBase {
  /**
   * Creates a new RunIntake.
   */
  private IntakeBase m_intakeBase;
  private BooleanSupplier m_finishedCondition;
  private Config m_intakeConfig = Config4905.getConfig4905().getIntakeConfig();
  private double m_intakeSpeed;

  /**
   * Runs the intake into the robot to intake balls at speed value set in the
   * config
   * 
   * @param intakeBase
   * @param finishedCondition
   */
  public DeployAndRunIntake(IntakeBase intakeBase, BooleanSupplier finishedCondition) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeBase);
    m_intakeBase = intakeBase;
    m_intakeSpeed = Config4905.getConfig4905().getCommandConstantsConfig().getDouble("DeployAndRunIntake.intakespeed");
    m_finishedCondition = finishedCondition;
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
    m_intakeBase.runIntake(m_intakeSpeed);
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
    return m_finishedCondition.getAsBoolean();
  }
}
