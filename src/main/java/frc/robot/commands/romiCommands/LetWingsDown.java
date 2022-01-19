package frc.robot.commands.romiCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.romiwings.RomiWingsBase;

public class LetWingsDown extends CommandBase {
  private Timer m_timer;
  private boolean m_usingTimer = false;
  private RomiWingsBase m_romiWings;
  private double m_time;

  public LetWingsDown(double time) {
    this();
    m_usingTimer = true;
    m_time = time;
    m_timer = new Timer();
  }

  public LetWingsDown() {
    m_romiWings = Robot.getInstance().getSubsystemsContainer().getWings();
    addRequirements(m_romiWings);
  }

  @Override
  public void initialize() {
    super.initialize();
    if (m_usingTimer) {
      m_timer.reset();
      m_timer.start();
    }
  }

  @Override
  public void execute() {
    super.execute();
    m_romiWings.letWingsDown();
  }

  @Override
  public boolean isFinished() {
    if (m_usingTimer) {
      return m_timer.hasElapsed(m_time);
    }
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    m_romiWings.stop();
  }

}
