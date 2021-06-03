package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.romiwings.RomiWingsBase;

public class BringWingsUp extends CommandBase {
  private RomiWingsBase m_romiWings;

  public BringWingsUp() {
    m_romiWings = Robot.getInstance().getSubsystemsContainer().getWings();
    addRequirements(m_romiWings);
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
  }
}
