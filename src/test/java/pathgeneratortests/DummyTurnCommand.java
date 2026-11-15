package pathgeneratortests;

import org.wpilib.command2.Command;

public class DummyTurnCommand extends Command {
  private double m_angle;

  public DummyTurnCommand(double angle) {
    m_angle = angle;
  }

  public double getAngle() {
    return m_angle;
  }

  public String toString() {
    return "Turn to angle: " + m_angle;
  }

}
