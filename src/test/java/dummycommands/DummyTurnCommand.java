package dummycommands;

public class DummyTurnCommand extends DummyCommandBase {
  private double m_angle;
  public static final double TOLERANCE = DummyMoveCommand.TOLERANCE;

  public DummyTurnCommand(double angle) {
    m_angle = angle;
  }

  public double getAngle() {
    return m_angle;
  }

  public String toString() {
    return "Turn to angle: " + m_angle;
  }

  @Override
  public boolean isEquivalentTo(DummyCommandBase d) {
    DummyTurnCommand dummyTurnCommand;
    if (!(d instanceof DummyTurnCommand)) {
      return false;
    }
    dummyTurnCommand = (DummyTurnCommand) d;
    return Math.abs(m_angle - dummyTurnCommand.m_angle) <= TOLERANCE;
  }

}
