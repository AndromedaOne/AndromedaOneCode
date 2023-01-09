// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.topGunIntakeCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.topGunIntake.IntakeBase;

public class RetractAndStopIntake extends CommandBase {
  /** Creates a new RetractAndStopIntake. */
  private IntakeBase m_intakeBase;

  public RetractAndStopIntake(IntakeBase intakeBase) {
    addRequirements(intakeBase);
    m_intakeBase = intakeBase;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_intakeBase.retractIntake();
    m_intakeBase.stopIntakeWheels();
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
