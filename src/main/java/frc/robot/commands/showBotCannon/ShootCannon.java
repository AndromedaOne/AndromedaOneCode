// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.ledlights.LEDRobotInformation;
import frc.robot.subsystems.showBotAudio.AudioFiles;
import frc.robot.subsystems.showBotAudio.ShowBotAudioBase;
import frc.robot.subsystems.showBotCannon.CannonBase;
import frc.robot.telemetries.Trace;

/* the shoot cannon button must be held down while the shoot cannon audio is playing. if the
 * button is released before the audio has completed playing, the shoot will be aborted. the
 * cannon will only shoot once the audio has completed.
 */
public class ShootCannon extends Command {
  /** Creates a new ShootCannon. */
  private CannonBase m_cannon;
  private ShowBotAudioBase m_audio;
  private int m_delayCount = 0;
  private boolean m_cannonShot = false;

  public ShootCannon() {
    m_cannon = Robot.getInstance().getSubsystemsContainer().getShowBotCannon();
    m_audio = Robot.getInstance().getSubsystemsContainer().getShowBotAudio();
    addRequirements(m_cannon.getSubsystemBase(), m_audio.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (!m_cannon.isPressurized()) {
      m_audio.playAudio(AudioFiles.CannonIsNotPressurized);
      return;
    }
    m_audio.playShootCannonAudio();
    m_delayCount = 0;
    m_cannonShot = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!m_cannon.isPressurized()) {
      return;
    }
    // we'll delay for a couple of seconds to wait for audio to start playing
    ++m_delayCount;
    if ((m_delayCount > 150) && !m_audio.isAudioPlaying()) {
      if (m_cannon.shoot()) {
        m_cannonShot = true;
        LEDRobotInformation.getInstance().setCannonIsPressurized(false);
        Trace.getInstance().logCommandInfo(this, "Cannon has been fired!!");
      } else {
        Trace.getInstance().logCommandInfo(this, "Something is blocking the cannon");
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_cannonShot || !m_cannon.isPressurized();
  }
}
