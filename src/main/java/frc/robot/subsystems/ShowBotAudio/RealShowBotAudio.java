// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.ShowBotAudio;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class RealShowBotAudio extends ShowBotAudioBase {
  // smartdashboard keys
  private final String m_audioFileToPlayKey = "showBotAudioFileToPlay";
  private final String m_audioIsPlayingKey = "showBotAudioIsPlaying";
  private final String m_showBotPiAudioPlayerConnectedKey = "showBotPiAudioConnected";
  private final String m_stopAudioKey = "showBotStopPiAudio";

  private final String m_showBotPiIsConnected = "ShowBotPiIsConnected";
  private final String m_roborioAckPiConnected = "RoborioAckPiConnected";

  private boolean m_audioisConnected = false;

  public RealShowBotAudio() {

  }

  @Override
  public void playAudio(String file) {
    if (!m_audioisConnected) {
      Trace.getInstance().logInfo(
          "ShowBotAudio: Warning: audio is not conntected. " + "Cannot play file: " + file);
      return;
    }
    SmartDashboard.putString(m_audioFileToPlayKey, file);
  }

  @Override
  public void stopAudio() {
    if (!m_audioisConnected) {
      Trace.getInstance()
          .logInfo("ShowBotAudio: Warning: audio is not conntected. " + "Cannot stop audio");
      return;
    }
    SmartDashboard.putBoolean(m_stopAudioKey, true);
  }

  @Override
  public boolean isAudioPlaying() {
    if (!m_audioisConnected) {
      Trace.getInstance().logInfo("ShowBotAudio: Warning: audio is not conntected. ");
      return false;
    }
    return SmartDashboard.getBoolean(m_audioIsPlayingKey, false);
  }

  @Override
  public void periodic() {
    if (!m_audioisConnected) {
      if (SmartDashboard.getString(m_showBotPiAudioPlayerConnectedKey, "")
          .equals(m_showBotPiIsConnected)) {
        SmartDashboard.putString(m_showBotPiAudioPlayerConnectedKey, m_roborioAckPiConnected);
        m_audioisConnected = true;
        Trace.getInstance().logInfo("Audio is connected");
      }
    }
  }
}
