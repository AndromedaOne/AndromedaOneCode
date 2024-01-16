// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.topGunShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.topGunShooter.ShooterAlignmentBase;

public class StopShooterAlignment extends Command {
  private ShooterAlignmentBase m_shooterAlignment;

  public StopShooterAlignment(ShooterAlignmentBase shooterAlignment) {
    m_shooterAlignment = shooterAlignment;
    addRequirements(shooterAlignment.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooterAlignment.stopShooterAlignment();
    m_shooterAlignment.stowShooterArms();
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
