// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.showBotAudio;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/** Add your docs here. */
public class MockShowBotAudio implements ShowBotAudioBase {

  @Override
  public void playAudio(AudioFiles audioFile) {
  }

  @Override
  public void stopAudio() {
  }

  @Override
  public boolean isAudioPlaying() {
    return false;
  }

  @Override
  public void playShootCannonAudio() {
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubsystemBase'");
  }

  @Override
  public void setDefaultCommand(CommandBase command) {
  }

}
