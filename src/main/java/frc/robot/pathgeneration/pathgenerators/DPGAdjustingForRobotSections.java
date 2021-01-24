package frc.robot.pathgeneration.pathgenerators;

import java.security.InvalidParameterException;

import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithRobotSide;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.AngleConversionUtils;
import frc.robot.utils.RobotDimensions;
import frc.robot.utils.RobotSections;

public abstract class DPGAdjustingForRobotSections extends DiagonalPathGenerator {
  private RobotDimensions m_robotDimensions;

  public DPGAdjustingForRobotSections(WaypointsBase waypoints, Waypoint initialWaypoint, double initialHeading,
      RobotDimensions robotDimensions) {
    super(waypoints, initialWaypoint);
    m_robotDimensions = robotDimensions;
    Waypoint initialWaypointAdjustedForRobotSide = getInitialPointAdjustedForRobotSide(initialWaypoint, initialHeading);
    super.setInitialPoint(initialWaypointAdjustedForRobotSide);

  }

  public DPGAdjustingForRobotSections(WaypointsBase waypoints, Waypoint initialWaypoint,
      RobotDimensions robotDimensions) {
    this(waypoints, initialWaypoint, Double.NaN, robotDimensions);
    if (initialWaypoint instanceof WaypointWithRobotSide) {
      throw new InvalidParameterException(
          "When giving a waypoint with robot side you must include the initial heading of the robot!");
    }
  }

  private Waypoint getInitialPointAdjustedForRobotSide(Waypoint initialWaypoint, double initialHeading) {
    Waypoint adjustedWaypoint = initialWaypoint.copy();
    if (initialWaypoint instanceof WaypointWithRobotSide) {
      WaypointWithRobotSide waypointWithRobotSide = (WaypointWithRobotSide) initialWaypoint;
      double distance = getDistanceAdjustmentFromRobotSection(waypointWithRobotSide.getSection());
      if (robotSectionAtFrontOfRobot(waypointWithRobotSide.getSection())) {
        adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, -distance, initialHeading);
      } else {
        adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, distance, initialHeading);
      }
    }
    return adjustedWaypoint;
  }

  @Override
  public void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
    double angle = getAngleToTurn(waypoint);

    // Only occurs when the robot does not have to move at all.
    if (Double.isNaN(angle)) {
      return;
    }

    WaypointWithRobotSide waypointWithRobotSide;
    if (!(waypoint instanceof WaypointWithRobotSide)) {
      waypointWithRobotSide = new WaypointWithRobotSide(waypoint, RobotSections.CENTER);
    } else {
      waypointWithRobotSide = (WaypointWithRobotSide) waypoint.copy();
    }

    if (isWaypointOnOrWithinTurningRadius(waypointWithRobotSide)) {
      addCommandsForPointWithinTurningRadius(waypointWithRobotSide, angle);
      return;
    }

    Waypoint adjustedWaypoint = getWaypointAdjustedForRobotSide(waypointWithRobotSide, angle);

    if (!robotSectionAtFrontOfRobot(waypointWithRobotSide.getSection())) {
      addCommandsForMovingBackOfRobot(adjustedWaypoint, angle);
      return;
    }
    super.generatePathForNextRelativeToStartWaypoint(adjustedWaypoint);
  }

  private boolean isWaypointOnOrWithinTurningRadius(WaypointWithRobotSide waypoint) {
    double distanceAdjustmentFromRobotSection = getDistanceAdjustmentFromRobotSection(waypoint.getSection());
    return waypoint.distance(getCurrentPointRelativeToInitialPoint()) <= distanceAdjustmentFromRobotSection;
  }

  private void addCommandsForPointWithinTurningRadius(WaypointWithRobotSide waypoint, double angle) {
    double distanceAdjustmentFromRobotSection = getDistanceAdjustmentFromRobotSection(waypoint.getSection());
    double distance = getCurrentPointRelativeToInitialPoint().distance(waypoint);
    if (robotSectionAtFrontOfRobot(waypoint.getSection())) {
      distance -= distanceAdjustmentFromRobotSection;
    } else {
      angle = AngleConversionUtils.ConvertAngleToCompassHeading(angle + 180);
      distance = distanceAdjustmentFromRobotSection - distance;
    }
    super.addTurnMoveCommands(distance, angle);
  }

  public void addCommandsForMovingBackOfRobot(Waypoint waypoint, double angle) {
    double distance = getCurrentPointRelativeToInitialPoint().distance(waypoint);
    angle += 180;
    angle = AngleConversionUtils.ConvertAngleToCompassHeading(angle);
    distance *= -1.0;
    super.addTurnMoveCommands(distance, angle);
  }

  private boolean robotSectionAtFrontOfRobot(RobotSections robotSections) {
    switch (robotSections) {
    case BACKOFBUMPER:
      return false;
    case BACKOFFRAME:
      return false;
    case FRONTOFFRAME:
      return true;
    case FRONTOFBUMPER:
      return true;
    default:
      break;
    }
    return true;
  }

  private Waypoint getWaypointAdjustedForRobotSide(WaypointWithRobotSide waypoint, double angle) {
    double distance = getDistanceAdjustmentFromRobotSection(waypoint.getSection());
    Waypoint adjustedWaypoint = getNewWaypointMovedInDirection(waypoint, -distance, angle);

    return adjustedWaypoint;

  }

  private double getDistanceAdjustmentFromRobotSection(RobotSections robotSections) {
    switch (robotSections) {
    case CENTER:
      return 0;
    case BACKOFBUMPER:
      return m_robotDimensions.getLength() * 0.5 + m_robotDimensions.getBumperThickness();
    case BACKOFFRAME:
      return m_robotDimensions.getLength() * 0.5;
    case FRONTOFFRAME:
      return m_robotDimensions.getLength() * 0.5;
    case FRONTOFBUMPER:
      return m_robotDimensions.getLength() * 0.5 + m_robotDimensions.getBumperThickness();
    }
    return 0;
  }

}
