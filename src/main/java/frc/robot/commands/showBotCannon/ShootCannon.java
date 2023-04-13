// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.showBotAudio.ShowBotAudioBase;
import frc.robot.subsystems.showBotCannon.CannonBase;
import frc.robot.telemetries.Trace;

/* the shoot cannon button must be held down while the shoot cannon audio is playing. if the
 * button is released before the audio has completed playing, the shoot will be aborted. the
 * cannon will only shoot once the audio has completed.
 */
public class ShootCannon extends CommandBase {
  /** Creates a new ShootCannon. */
  private CannonBase m_cannon;
  private ShowBotAudioBase m_audio;
  private int m_delayCount = 0;
  private boolean m_cannonShot = false;

  public ShootCannon() {
    m_cannon = Robot.getInstance().getSubsystemsContainer().getShowBotCannon();
    m_audio = Robot.getInstance().getSubsystemsContainer().getShowBotAudio();
    addRequirements(m_cannon, m_audio);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_audio.playShootCannonAudio();
    m_delayCount = 0;
    m_cannonShot = false;
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // we'll delay for a couple of seconds to wait for audio to start playing
    if ((m_delayCount > 50) && !m_audio.isAudioPlaying()) {
      m_cannon.shoot();
      m_cannonShot = true;
      Trace.getInstance().logCommandInfo(this, "Cannon has been fired!!");
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_audio.stopAudio();
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_cannonShot;
  }
}
