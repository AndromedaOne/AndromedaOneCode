// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.showBotCannon.CannonBase;

public class PressurizeCannon extends CommandBase {
  /** Creates a new PressurizeCannon. */
  private CannonBase m_cannon;

  public PressurizeCannon() {
    m_cannon = Robot.getInstance().getSubsystemsContainer().getCannon();
    addRequirements(m_cannon);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_cannon.pressurize();
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
