package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Config4905;
import frc.robot.telemetries.Trace;

public class ConfigReload extends CommandBase {
  public ConfigReload() {

  }

  @Override
  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart(this);
  }

  @Override
  public void execute() {
    Config4905.reload();
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
  }
}
