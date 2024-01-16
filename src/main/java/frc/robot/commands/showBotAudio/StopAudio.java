// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotAudio;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.showBotAudio.ShowBotAudioBase;
import frc.robot.telemetries.Trace;

public class StopAudio extends Command {
  ShowBotAudioBase m_audio;

  /** Creates a new StopAudio. */
  public StopAudio(ShowBotAudioBase audio) {
    m_audio = audio;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    m_audio.stopAudio();
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
