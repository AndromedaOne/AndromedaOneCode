// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sbsdAlgaeManipulatorCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.sbsdAlgaeManipulator.SBSDAlgaeManipulatorBase;

/** Add your docs here. */
public class MoveAlgaeManipulator extends Command {
  private SBSDAlgaeManipulatorBase m_algaeManipulator;

  public MoveAlgaeManipulator() {
    m_algaeManipulator = Robot.getInstance().getSubsystemsContainer().getSBSDAlgaeManipulatorBase();
    addRequirements(m_algaeManipulator.getSubsystemBase());
    SmartDashboard.putNumber("SBSD Set Algae Speed", 0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speed = SmartDashboard.getNumber("SBSD Set Algae Speed", 0);
    m_algaeManipulator.moveUsingSmartDashboard(speed);
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
