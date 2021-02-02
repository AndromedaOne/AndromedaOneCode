package frc.robot.pathgeneration.waypoints;

import frc.robot.utils.AngleConversionUtils;

public class WaypointWithHeading extends Waypoint {

  double m_heading;

  public WaypointWithHeading(double x, double y, double heading) {
    super(x, y);
    m_heading = AngleConversionUtils.ConvertAngleToCompassHeading(heading);
  }

  public double getHeading() {
    return m_heading;
  }

  public Waypoint subtract(WaypointWithHeading w) {
    double deltaX = super.getX() - w.getX();
    double deltaY = super.getY() - w.getY();
    double deltaHeading = AngleConversionUtils.ConvertAngleToCompassHeading(m_heading - w.getHeading());

    return new WaypointWithHeading(deltaX, deltaY, deltaHeading);
  }
}
