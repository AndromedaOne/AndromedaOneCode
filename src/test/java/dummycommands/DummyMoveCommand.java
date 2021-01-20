package dummycommands;

public class DummyMoveCommand extends DummyCommandBase {
  private double m_distance;
  private double m_angle;
  public static int numberOfDummyMoveCommandInstances = 0;
  public static final double TOLERANCE = 0.1;

  public DummyMoveCommand(double distance, double angle) {
    m_distance = distance;
    m_angle = angle;
    numberOfDummyMoveCommandInstances++;

  }

  public double getDistance() {
    return m_distance;
  }

  public double getAngle() {
    return m_angle;
  }

  public String toString() {
    return "Move distance: " + m_distance + " at angle: " + m_angle;
  }

  @Override
  public boolean isEquivalentTo(DummyCommandBase d) {
    DummyMoveCommand dummyMoveCommand;
    if (!(d instanceof DummyMoveCommand)) {
      return false;
    }
    dummyMoveCommand = (DummyMoveCommand) d;
    return Math.abs(m_distance - dummyMoveCommand.m_distance) <= TOLERANCE
        && Math.abs(m_angle - dummyMoveCommand.m_angle) <= TOLERANCE;
  }

}
