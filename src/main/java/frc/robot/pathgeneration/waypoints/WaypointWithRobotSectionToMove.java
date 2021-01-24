package frc.robot.pathgeneration.waypoints;

import frc.robot.utils.RobotSectionsForWaypointsToMove;

// A robot section for waypoints to move is a part of the robot that you want waypoints to end at. For example you may want the front bumper of the robot to move to the waypoint 3,3 so you would create a WaypointWithRobotSectionToMove and pass in the front bumper section as the RobotSectionsForWaypointsToMove.

public class WaypointWithRobotSectionToMove extends Waypoint {

  private RobotSectionsForWaypointsToMove m_robotSectionToMove;

  public WaypointWithRobotSectionToMove(double x, double y, RobotSectionsForWaypointsToMove robotSectionToMove) {
    super(x, y);
    m_robotSectionToMove = robotSectionToMove;
  }

  public WaypointWithRobotSectionToMove(Waypoint w, RobotSectionsForWaypointsToMove robotside) {
    this(w.getX(), w.getY(), robotside);
  }

  public RobotSectionsForWaypointsToMove getRobotSectionToMove() {
    return m_robotSectionToMove;
  }

  @Override
  public Waypoint add(Waypoint w) {
    return new WaypointWithRobotSectionToMove(super.add(w), m_robotSectionToMove);
  }

  @Override
  public Waypoint subtract(Waypoint w) {
    return new WaypointWithRobotSectionToMove(super.subtract(w), m_robotSectionToMove);
  }

  @Override
  public Waypoint multiply(double factor) {
    return new WaypointWithRobotSectionToMove(super.multiply(factor), m_robotSectionToMove);
  }

  @Override
  public Waypoint divide(double factor) {
    return new WaypointWithRobotSectionToMove(super.divide(factor), m_robotSectionToMove);
  }

  @Override
  public WaypointWithRobotSectionToMove copy() {
    return new WaypointWithRobotSectionToMove(super.getX(), super.getY(), m_robotSectionToMove);
  }
}
