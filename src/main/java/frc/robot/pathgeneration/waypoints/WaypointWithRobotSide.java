package frc.robot.pathgeneration.waypoints;

import frc.robot.utils.RobotSections;

public class WaypointWithRobotSide extends Waypoint {

  private RobotSections m_robotSides;

  public WaypointWithRobotSide(double x, double y, RobotSections robotside) {
    super(x, y);
    m_robotSides = robotside;
  }

  public WaypointWithRobotSide(Waypoint w, RobotSections robotside) {
    this(w.getX(), w.getY(), robotside);
  }

  public RobotSections getSection() {
    return m_robotSides;
  }

  @Override
  public Waypoint subtract(Waypoint w) {
    return new WaypointWithRobotSide(super.subtract(w), m_robotSides);
  }

  @Override
  public Waypoint add(Waypoint w) {
    return new WaypointWithRobotSide(super.add(w), m_robotSides);
  }

  @Override
  public Waypoint multiply(double factor) {
    return new WaypointWithRobotSide(super.multiply(factor), m_robotSides);
  }

  @Override
  public Waypoint divide(double factor) {
    return new WaypointWithRobotSide(super.divide(factor), m_robotSides);
  }

  @Override
  public WaypointWithRobotSide copy() {
    return new WaypointWithRobotSide(super.getX(), super.getY(), m_robotSides);
  }
}
