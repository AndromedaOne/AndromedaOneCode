// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.actuators.ServoMotor;
import frc.robot.telemetries.Trace;

public class ToggleConveyor extends CommandBase {
  private ServoMotor m_conveyor;

  /** Creates a new StartConveyor. */
  public ToggleConveyor(ServoMotor conveyor) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_conveyor = conveyor;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    if (Robot.getInstance().getSubsystemsContainer().getConveyorState()) {
      m_conveyor.stop();
      Robot.getInstance().getSubsystemsContainer().setConveyorState(false);
    } else {
      m_conveyor.runForward();
      Robot.getInstance().getSubsystemsContainer().setConveyorState(true);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
