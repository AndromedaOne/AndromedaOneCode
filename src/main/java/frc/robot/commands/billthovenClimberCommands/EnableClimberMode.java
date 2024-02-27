// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.billthovenClimberCommands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.billthovenArmRotateCommands.ArmRotate;
import frc.robot.subsystems.billArmRotate.BillArmRotateBase;
import frc.robot.subsystems.billClimber.BillClimberBase;
import frc.robot.telemetries.Trace;

public class EnableClimberMode extends Command {
  /** Creates a new EnableClimberMode. */
  private BillClimberBase m_climber;
  private BillArmRotateBase m_armRotate;

  public EnableClimberMode(BillClimberBase climber, BillArmRotateBase armRotate) {
    m_climber = climber;
    m_armRotate = armRotate;
    addRequirements(m_climber.getSubsystemBase(), m_armRotate.getSubsystemBase());
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double armAngleForClimbing = 255;
    CommandScheduler.getInstance().removeDefaultCommand(m_climber.getSubsystemBase());
    CommandScheduler.getInstance().removeDefaultCommand(m_armRotate.getSubsystemBase());
    CommandScheduler.getInstance().setDefaultCommand(m_climber.getSubsystemBase(),
        new RunBillClimber(m_climber, false));
    CommandScheduler.getInstance().setDefaultCommand(m_armRotate.getSubsystemBase(),
        new ArmRotate(m_armRotate, () -> armAngleForClimbing, false, false));
    Trace.getInstance().logCommandInfo(this, "Enable Climber Mode Ran");
    Trace.getInstance().logCommandInfo(this,
        CommandScheduler.getInstance().getDefaultCommand(m_climber.getSubsystemBase()).getName());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
