package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.romiShooter.StopRomiShooter;
import frc.robot.subsystems.romiwings.RomiWingsBase;
import frc.robot.telemetries.Trace;

public class BringWingsUp extends CommandBase {
  private RomiWingsBase m_romiWings;

  public BringWingsUp() {
    m_romiWings = Robot.getInstance().getSubsystemsContainer().getWings();
    addRequirements(m_romiWings);
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void execute() {
    super.execute();
    m_romiWings.bringWingsUp();
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    m_romiWings.stop();
    Trace.getInstance().logCommandStop(this);

  }
}
