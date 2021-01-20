package frc.robot.pathgeneration.waypoints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class WaypointsBase implements Iterable<Waypoint> {

  private List<Waypoint> m_waypoints;
  private boolean waypointsLoaded;

  public WaypointsBase() {
    m_waypoints = new ArrayList<Waypoint>();
    waypointsLoaded = false;
  }

  protected abstract void loadWaypoints();

  @Override
  public Iterator<Waypoint> iterator() {
    if(!waypointsLoaded){
      loadWaypoints();
      waypointsLoaded = true;
    }
    return m_waypoints.iterator();
  }

  protected void addWayPoint(Waypoint e) {
    m_waypoints.add(e);
  }

}
