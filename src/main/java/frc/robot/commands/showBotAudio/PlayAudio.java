// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotAudio;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.showBotAudio.AudioFiles;
import frc.robot.subsystems.showBotAudio.ShowBotAudioBase;

public class PlayAudio extends Command {
  ShowBotAudioBase m_audio;
  AudioFiles m_audioFile;

  /** Creates a new PlayAudio. */
  public PlayAudio(ShowBotAudioBase audio, AudioFiles audioFile) {
    m_audio = audio;
    m_audioFile = audioFile;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_audio.playAudio(m_audioFile);
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
