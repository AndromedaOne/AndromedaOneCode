// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class DelayedSequentialCommandGroup extends SequentialCommandGroup {
  /*
   * Inserts a delay from SmartDashboard before executing the sequential commands.
   */

  /** Creates a new DelayedSequentialCommandGroup. */
  public DelayedSequentialCommandGroup(Command... commands) {
    addCommands(new SpecialWaitCommand());
    addCommands(commands);
  }

  private class SpecialWaitCommand extends CommandBase {
    protected Timer m_timer = new Timer();
    private double m_duration;

    @Override
    public void initialize() {
      m_duration = SmartDashboard.getNumber("Auto Delay", 0);
      m_timer.reset();
      m_timer.start();
      Trace.getInstance().logCommandStart(this);
      Trace.getInstance().logCommandInfo(this, "Auto Delay; " + m_duration);
    }

    @Override
    public void end(boolean interrupted) {
      m_timer.stop();
      Trace.getInstance().logCommandStop(this);
    }

    @Override
    public boolean isFinished() {
      return m_timer.advanceIfElapsed(m_duration);
    }

    @Override
    public boolean runsWhenDisabled() {
      return true;
    }
  }
}
