/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.telemetries.Trace;

public class ExtendIntake extends CommandBase {
  /**
   * Creates a new ExtendIntake.
   */
  private IntakeBase m_intakeBase;
  BooleanSupplier m_endCondition;

  public ExtendIntake(IntakeBase intakeBase, BooleanSupplier endCondition) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intakeBase);
    m_intakeBase = intakeBase;
    m_endCondition = endCondition;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_intakeBase.deployIntake();
    Trace.getInstance().logCommandStart("ExtendIntake");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop("ExtendIntake");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_endCondition.getAsBoolean();
  }
}
