package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.romiwings.RomiWingsBase;

public class BringWingsUp extends CommandBase {
  private RomiWingsBase romiWings;

  public BringWingsUp() {
    romiWings = Robot.getInstance().getSubsystemsContainer().getWings();
    addRequirements(romiWings);
  }

  @Override
  public void execute() {
    super.execute();
    romiWings.bringWingsUp();
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    romiWings.stop();
  }
}
