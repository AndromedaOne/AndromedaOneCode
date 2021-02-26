package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public abstract class PathGeneratorBase {

  private WaypointsBase m_waypoints;

  public PathGeneratorBase(WaypointsBase waypoints) {
    m_waypoints = waypoints;
  }

  protected void setInitialWaypoint(Waypoint initialPoint) {
    m_initialPoint = initialPoint;
  }

  protected Waypoint getInitialWaypoint() {
    return m_initialPoint;
  }

  public CommandBase getPath() {
    System.out.println("\nGenerating New Path");
    iterateThroughWaypointsToGeneratePath();
    return getGeneratedPath();
  }

  private void iterateThroughWaypointsToGeneratePath() {
    for (Waypoint w : m_waypoints) {
      generatePathForNextWaypoint(w);
    }
  }

  protected abstract void generatePathForNextWaypoint(Waypoint waypoint);

  protected abstract CommandBase getGeneratedPath();

}
