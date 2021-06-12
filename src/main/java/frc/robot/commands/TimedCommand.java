package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.telemetries.Trace;

public class TimedCommand extends CommandBase {
  private Timer timer;
  private double m_time;

  public static CommandBase create(CommandBase command, double time) {
    ParallelDeadlineGroup parallelDeadlineGroup = new ParallelDeadlineGroup(new TimedCommand(time), command);
    return parallelDeadlineGroup;
  }

  public static CommandBase create(double time) {
    return new TimedCommand(time);
  }

  private TimedCommand(double time) {
    timer = new Timer();
    m_time = time;
  }

  @Override
  public void initialize() {
    super.initialize();
    timer.reset();
    timer.start();
    Trace.getInstance().logCommandStart(this);
    Trace.getInstance().logCommandInfo(this, "start m_time: " + m_time);
  }

  @Override
  public boolean isFinished() {
    return timer.hasElapsed(m_time);
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
    Trace.getInstance().logCommandInfo(this, "end m_time: " + m_time);
  }
}
