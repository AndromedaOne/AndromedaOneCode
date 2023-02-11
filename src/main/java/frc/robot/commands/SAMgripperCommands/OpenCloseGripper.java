// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.SAMgripperCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.SAMgripper.GripperBase;
import frc.robot.telemetries.Trace;

public class OpenCloseGripper extends CommandBase {
  /** Creates a new OpenCloseGripper. */
  protected GripperBase m_gripper;
  protected int m_state;
  // protected SubsystemsContainer m_subsystemscontainer;

  public OpenCloseGripper(GripperBase gripper, int State) {
    // Use addRequirements() here to declare subsystem dependencies
    m_gripper = gripper;
    addRequirements(m_gripper);
    m_state = State;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getGripperButtonPressed() == true) {
      if (m_state == 0) {
        m_state = 1;
        m_gripper.closeGripper();
        Trace.getInstance().logCommandInfo(this, "Gripper closed");
      } else if (m_state == 1) {
        m_state = 0;
        m_gripper.openGripper();
        Trace.getInstance().logCommandInfo(this, "Gripper opened");
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
