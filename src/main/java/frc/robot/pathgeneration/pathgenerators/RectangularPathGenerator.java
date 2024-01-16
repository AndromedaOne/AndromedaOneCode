package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public abstract class RectangularPathGenerator extends PathGeneratorBase {

  private Waypoint m_currentWaypoint;
  private SequentialCommandGroup m_path;

  public RectangularPathGenerator(String pathName, WaypointsBase waypoints,
      Waypoint initialWaypoint) {
    super(pathName, waypoints);
    m_path = new SequentialCommandGroup();
    m_currentWaypoint = initialWaypoint;
  }

  @Override
  protected void generatePathForNextWaypoint(Waypoint waypoint) {
    System.out.print("Waypoint 1: ");
    m_currentWaypoint.prettyPrint();
    System.out.print("Waypoint 2: ");
    waypoint.prettyPrint();
    double deltaY = waypoint.getY() - m_currentWaypoint.getY();
    double deltaX = waypoint.getX() - m_currentWaypoint.getX();

    if (deltaY > 0) {
      m_path.addCommands(createTurnCommand(0), createMoveCommand(deltaY, 0));
    } else if (deltaY < 0) {
      m_path.addCommands(createTurnCommand(180), createMoveCommand(Math.abs(deltaY), 180));
    }
    if (deltaX > 0) {
      m_path.addCommands(createTurnCommand(90), createMoveCommand(deltaX, 90));
    } else if (deltaX < 0) {
      m_path.addCommands(createTurnCommand(270), createMoveCommand(Math.abs(deltaX), 270));
    }

    m_currentWaypoint = waypoint;
  }

  protected abstract Command createTurnCommand(double angle);

  protected abstract Command createMoveCommand(double distance, double angle);

  @Override
  protected Command getGeneratedPath() {
    return m_path;
  }

}
