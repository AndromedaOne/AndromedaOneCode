package frc.robot.pathgeneration.waypoints;

public class Waypoints extends WaypointsBase {

  Waypoint[] m_waypoints;

  public Waypoints(Waypoint... waypoints) {
    m_waypoints = waypoints;
  }

  @Override
  protected void loadWaypoints() {
    for (Waypoint w : m_waypoints) {
      addWayPoint(w);
    }
  }
}
