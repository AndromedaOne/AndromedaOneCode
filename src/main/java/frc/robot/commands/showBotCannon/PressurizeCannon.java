// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.showBotCannon;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.ledlights.LEDRobotInformation;
import frc.robot.subsystems.showBotAudio.AudioFiles;
import frc.robot.subsystems.showBotCannon.CannonBase;
import frc.robot.telemetries.Trace;

public class PressurizeCannon extends CommandBase {
  /** Creates a new PressurizeCannon. */
  private CannonBase m_cannon;
  private int m_counter = 0;

  public PressurizeCannon() {
    m_cannon = Robot.getInstance().getSubsystemsContainer().getShowBotCannon();
    addRequirements(m_cannon.getSubsystemBase());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_cannon.pressurize();
    LEDRobotInformation.getInstance().setCannonIsPressurized(true);
    Trace.getInstance().logCommandStart(this);
    m_counter = 0;
    Robot.getInstance().getSubsystemsContainer().getShowBotAudio()
        .playAudio(AudioFiles.CannonIsPressurized);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_counter++;
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_counter > 150) {
      return true;
    }
    return false;
  }
}
