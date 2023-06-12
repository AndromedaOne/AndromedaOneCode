// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.SAMgripperCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SAMgripper.GripperBase;

public class CloseGripper extends CommandBase {
  /** Creates a new CloseGripper. */
  GripperBase m_gripperBase;

  public CloseGripper(GripperBase gripperBase) {
    addRequirements(gripperBase.getSubsystemBase());
    m_gripperBase = gripperBase;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_gripperBase.closeGripper();
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
