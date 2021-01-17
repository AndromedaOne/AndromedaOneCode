package frc.robot.pathgeneration.waypoints;

import java.util.Iterator;
import java.util.List;

public abstract class WaypointsBase implements Iterable<Waypoint> {

  private List<Waypoint> m_waypoints;

  public WaypointsBase() {
    loadWaypoints();
  }

  protected abstract void loadWaypoints();

  @Override
  public Iterator<Waypoint> iterator() {
    return m_waypoints.iterator();
  }

  protected void addWayPoint(Waypoint e) {
    m_waypoints.add(e);
  }

}
