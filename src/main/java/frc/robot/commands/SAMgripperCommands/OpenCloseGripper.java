// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.SAMgripperCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.SAMgripper.GripperBase;
import frc.robot.subsystems.SAMgripper.GripperState;
import frc.robot.telemetries.Trace;

public class OpenCloseGripper extends CommandBase {
  /** Creates a new OpenCloseGripper. */
  protected GripperBase m_gripper;
  protected int m_state;

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

    if (m_state == GripperState.OPENGRIPPER.ordinal()) {
      m_gripper.closeGripper();
    } else if (m_state == GripperState.CLOSEGRIPPER.ordinal()) {
      m_gripper.openGripper();
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    if (m_state == GripperState.OPENGRIPPER.ordinal()) {
      m_state = GripperState.CLOSEGRIPPER.ordinal();
    } else if (m_state == GripperState.CLOSEGRIPPER.ordinal()) {
      m_state = GripperState.OPENGRIPPER.ordinal();
    }
    SmartDashboard.putNumber("New Gripper state =", m_state);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
