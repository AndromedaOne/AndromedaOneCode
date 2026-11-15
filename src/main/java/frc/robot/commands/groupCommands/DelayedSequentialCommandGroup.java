// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import org.wpilib.system.Timer;
import org.wpilib.smartdashboard.SmartDashboard;
import org.wpilib.command2.Command;
import org.wpilib.command2.SequentialCommandGroup;
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

  private class SpecialWaitCommand extends Command {
    protected Timer m_timer = new Timer();
    private double m_duration;

    @Override
    public void initialize() {
      m_duration = SmartDashboard.getNumber("Auto Delay", 0);
      m_timer.reset();
      m_timer.start();
      Trace.getInstance().logCommandInfo(this, "Auto Delay; " + m_duration);
    }

    @Override
    public void end(boolean interrupted) {
      m_timer.stop();
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
