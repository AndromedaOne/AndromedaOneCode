package dummycommands;

import edu.wpi.first.wpilibj2.command.CommandBase;

public abstract class DummyCommandBase extends CommandBase {

  public abstract boolean isEquivalentTo(DummyCommandBase d);

  @Override
  public void execute() {
    // TODO Auto-generated method stub
    super.execute();
    DummyPathChecker.registerCommandAsRunning(this);
  }

  @Override
  public boolean isFinished() {
    return true;
  }

}
