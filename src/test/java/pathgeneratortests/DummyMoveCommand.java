package pathgeneratortests;

import org.wpilib.command2.Command;

public class DummyMoveCommand extends Command {
  private double m_distance;
  private double m_angle;
  public static int numberOfDummyMoveCommandInstances = 0;

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

}
