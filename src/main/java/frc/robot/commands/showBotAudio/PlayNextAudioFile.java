// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotAudio;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.showBotAudio.AudioFiles;
import frc.robot.subsystems.showBotAudio.ShowBotAudioBase;

public class PlayNextAudioFile extends Command {
  ShowBotAudioBase m_audio;
  static List<AudioFiles> m_audioFiles = Arrays.asList(AudioFiles.AlsoSprachZarathustra,
      AudioFiles.CrazyTrain, AudioFiles.DeadMansParty, AudioFiles.DreamTheater,
      AudioFiles.DroidProblem, AudioFiles.HellsBells, AudioFiles.ManhattanProject,
      AudioFiles.Paranoimia, AudioFiles.SolarAnthem, AudioFiles.TheBigMoney);
  static ListIterator<AudioFiles> m_audioToPlay = m_audioFiles.listIterator();

  /** Creates a new PlayNextAudioFile. */
  public PlayNextAudioFile(ShowBotAudioBase audio) {
    m_audio = audio;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (m_audioToPlay.hasNext()) {
      m_audio.playAudio(m_audioToPlay.next());
    } else {
      m_audioToPlay = m_audioFiles.listIterator();
      m_audio.playAudio(m_audioFiles.get(0));
    }
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
