package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.romishooter.RomiShooterBase;

public class RunRomiShooter extends CommandBase {
  RomiShooterBase romiShooter;

  public RunRomiShooter() {
    romiShooter = Robot.getInstance().getSubsystemsContainer().getRomiShooter();
    addRequirements(romiShooter);
  }

  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public void execute() {
    super.execute();
    romiShooter.setSpeed(0.5);
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    romiShooter.setSpeed(0.0);
  }

}
