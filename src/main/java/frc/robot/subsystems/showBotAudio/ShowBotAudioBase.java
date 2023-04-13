// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotAudio;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ShowBotAudioBase extends SubsystemBase {
  /** Creates a new ShowBotAudioBase. */
  public ShowBotAudioBase() {
  }

  public abstract void playShootCannonAudio();

  public abstract void playAudio(String file);

  public abstract void stopAudio();

  public abstract boolean isAudioPlaying();

}
