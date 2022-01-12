package frc.robot.groupcommands.sequentialgroup;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DelayedSequentialCommandGroup extends SequentialCommandGroup {
  /*
   * Uses a copied version of WPILib's WaitCommand, which grabs the delay from
   * SmartDashboard instead of in the constructor. Then adds the commands same as
   * a normal SequentialCommandGroup.
   */
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
      System.out.println("Auto Delay = " + m_duration);
      SendableRegistry.setName(this, getName() + ": " + m_duration + " seconds");
      m_timer.reset();
      m_timer.start();
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