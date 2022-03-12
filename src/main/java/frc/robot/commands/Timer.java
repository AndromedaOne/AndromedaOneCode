// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.time.Instant;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.telemetries.Trace;

public class Timer extends CommandBase {
  private long m_duration = 0;
  private Instant m_endTime;

  /** Creates a new Timer. */
  public Timer(long durationInMilliSeconds) {
    m_duration = durationInMilliSeconds;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_endTime = Instant.now().plusMillis(m_duration);
    Trace.getInstance().logCommandInfo(this,
        "Current Time: " + Instant.now().toEpochMilli() + " End Time: " + m_endTime.toEpochMilli());
    Trace.getInstance().logCommandStart(this);
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
    int result = m_endTime.compareTo(Instant.now());
    // if the current time is less than the end time, then the result will be
    // positive
    // if the times are equal, then the result will be 0
    // otherwise the result is negative
    return result <= 0;
  }
}
