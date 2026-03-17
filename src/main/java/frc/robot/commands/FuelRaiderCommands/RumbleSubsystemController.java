// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.FuelRaiderCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.oi.SubsystemController;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class RumbleSubsystemController extends Command {
  /** Creates a new RumbleSubsystemController. */
  private SubsystemController m_subsystemController;
  private double m_strength = 0;

  public RumbleSubsystemController(double strength) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
    m_strength = strength;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystemController.rumbleOn(m_strength);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystemController.rumbleOff();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
