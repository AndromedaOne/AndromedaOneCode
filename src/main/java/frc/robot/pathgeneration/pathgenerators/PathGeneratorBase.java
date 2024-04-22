package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public abstract class PathGeneratorBase {

  private WaypointsBase m_waypoints;

  public PathGeneratorBase(String pathName, WaypointsBase waypoints) {
    System.out.println("Generating path for " + pathName);
    m_waypoints = waypoints;
  }

  public Command getPath() {
    iterateThroughWaypointsToGeneratePath();
    System.out.println("End of generated path\n");
    return getGeneratedPath();
  }

  private void iterateThroughWaypointsToGeneratePath() {
    for (Waypoint w : m_waypoints) {
      generatePathForNextWaypoint(w);
    }
  }

  protected abstract void generatePathForNextWaypoint(Waypoint waypoint);

  protected abstract Command getGeneratedPath();

}
