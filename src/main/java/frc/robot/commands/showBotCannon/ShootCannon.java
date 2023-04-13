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

  public ShootCannon() {
    m_cannon = Robot.getInstance().getSubsystemsContainer().getShowBotCannon();
    m_audio = Robot.getInstance().getSubsystemsContainer().getShowBotAudio();
    addRequirements(m_cannon, m_audio);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_audio.playShootCannonAudio();
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_cannon.shoot();
    Trace.getInstance().logCommandStart(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_audio.isAudioPlaying()) {
      return false;
    }
    return true;
  }
}
