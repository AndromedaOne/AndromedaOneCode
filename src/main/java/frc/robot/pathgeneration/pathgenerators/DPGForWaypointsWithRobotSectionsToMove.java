package frc.robot.pathgeneration.pathgenerators;

import java.security.InvalidParameterException;

import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithRobotSectionToMove;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.AngleConversionUtils;
import frc.robot.utils.RobotDimensions;
import frc.robot.utils.RobotSectionsForWaypointsToMove;

// DPG stands for Diagonal Path Generator. This path generator will take into account whether a waypoint wants the center section of the robot moved to a specific point, or if just wants the front of the frame moved to a point etc.
public abstract class DPGForWaypointsWithRobotSectionsToMove extends DiagonalPathGenerator {
  private RobotDimensions m_robotDimensions;

  public DPGForWaypointsWithRobotSectionsToMove(WaypointsBase waypoints, Waypoint initialWaypoint,
      double initialHeading, RobotDimensions robotDimensions) {
    super(waypoints, initialWaypoint);
    m_robotDimensions = robotDimensions;
    Waypoint initialWaypointAtCenterOfRobot = getInitialWaypointAtCenterOfRobot(initialWaypoint, initialHeading);
    super.setInitialPoint(initialWaypointAtCenterOfRobot);

  }

  public DPGForWaypointsWithRobotSectionsToMove(WaypointsBase waypoints, Waypoint initialWaypoint,
      RobotDimensions robotDimensions) {
    this(waypoints, initialWaypoint, Double.NaN, robotDimensions);
    if (initialWaypoint instanceof WaypointWithRobotSectionToMove) {
      throw new InvalidParameterException(
          "When giving a waypoint with robot section you must include the initial heading of the robot!");
    }
  }

  private Waypoint getInitialWaypointAtCenterOfRobot(Waypoint initialWaypoint, double initialHeading) {
    Waypoint waypointAtCenterOfRobot = initialWaypoint.copy();

    if (initialWaypoint instanceof WaypointWithRobotSectionToMove) {
      WaypointWithRobotSectionToMove waypointWithRobotSectionToMove = (WaypointWithRobotSectionToMove) initialWaypoint;
      double distanceRobotSectionIsFromTurningCenterOfRobot = getDistanceFromTurningCenterForRobotSection(
          waypointWithRobotSectionToMove.getRobotSectionToMove());

      if (robotSectionInFrontOfTurningCenter(waypointWithRobotSectionToMove.getRobotSectionToMove())) {
        waypointAtCenterOfRobot = getNewWaypointMovedInDirection(waypointWithRobotSectionToMove,
            -distanceRobotSectionIsFromTurningCenterOfRobot, initialHeading);
      } else {
        waypointAtCenterOfRobot = getNewWaypointMovedInDirection(waypointWithRobotSectionToMove,
            distanceRobotSectionIsFromTurningCenterOfRobot, initialHeading);
      }
    }
    return waypointAtCenterOfRobot;
  }

  @Override
  public void generatePathForNextRelativeToStartWaypoint(Waypoint nextWaypointRelativeToInitialPoint) {
    double angleToNextWaypoint = getAngleToTurn(nextWaypointRelativeToInitialPoint);

    // Only occurs when the robot does not have to move at all.
    if (Double.isNaN(angleToNextWaypoint)) { 
      return;
    }

    WaypointWithRobotSectionToMove nextWaypointWithRobotSectionToMove;
    if (!(nextWaypointRelativeToInitialPoint instanceof WaypointWithRobotSectionToMove)) {
      nextWaypointWithRobotSectionToMove = new WaypointWithRobotSectionToMove(nextWaypointRelativeToInitialPoint,
          RobotSectionsForWaypointsToMove.CENTER);
    } else {
      nextWaypointWithRobotSectionToMove = (WaypointWithRobotSectionToMove) nextWaypointRelativeToInitialPoint.copy();
    }

    if (isNextWaypointOnOrWithinTurningRadius(nextWaypointWithRobotSectionToMove)) {
      addCommandsForPointWithinTurningRadius(nextWaypointWithRobotSectionToMove, angleToNextWaypoint);
      return;
    }

    Waypoint nextWaypointForCenterOfRobot = getNextWaypointForCenterOfRobot(nextWaypointWithRobotSectionToMove,
        angleToNextWaypoint);

    if (!robotSectionInFrontOfTurningCenter(nextWaypointWithRobotSectionToMove.getRobotSectionToMove())) {
      addCommandsForMovingBackwards(nextWaypointForCenterOfRobot, angleToNextWaypoint);
      return;
    }

    if (isNextWaypointSectionToMoveOnTurningCenter(nextWaypointWithRobotSectionToMove)
        && isQuickerToMoveCenterOfRobotBackwards(angleToNextWaypoint)) {
      addCommandsForMovingBackwards(nextWaypointForCenterOfRobot, angleToNextWaypoint);
      return;
    }
    super.generatePathForNextRelativeToStartWaypoint(nextWaypointForCenterOfRobot);
  }

  private boolean isNextWaypointOnOrWithinTurningRadius(WaypointWithRobotSectionToMove nextWaypoint) {
    double turningRadiusForRobotSection = getDistanceFromTurningCenterForRobotSection(
        nextWaypoint.getRobotSectionToMove());
    return nextWaypoint.distance(getCurrentPointRelativeToInitialPoint()) <= turningRadiusForRobotSection;
  }

  private void addCommandsForPointWithinTurningRadius(WaypointWithRobotSectionToMove nextWaypoint,
      double angleToNextWaypoint) {
    double turningRadiusForRobotSection = getDistanceFromTurningCenterForRobotSection(
        nextWaypoint.getRobotSectionToMove());
    double distanceToNextWaypoint = getCurrentPointRelativeToInitialPoint().distance(nextWaypoint);
    double distanceToMove = 0;
    if (robotSectionInFrontOfTurningCenter(nextWaypoint.getRobotSectionToMove())) {
      distanceToMove = distanceToNextWaypoint - turningRadiusForRobotSection;
    } else {
      angleToNextWaypoint = AngleConversionUtils.ConvertAngleToCompassHeading(angleToNextWaypoint + 180);
      distanceToMove = turningRadiusForRobotSection - distanceToNextWaypoint;
    }
    super.addTurnMoveCommandsToPathAndMoveCurrentPoint(distanceToMove, angleToNextWaypoint);
  }

  public void addCommandsForMovingBackwards(Waypoint nextWaypoint, double angleToNextWaypoint) {
    double distanceToNextWaypoint = getCurrentPointRelativeToInitialPoint().distance(nextWaypoint);
    angleToNextWaypoint += 180;
    angleToNextWaypoint = AngleConversionUtils.ConvertAngleToCompassHeading(angleToNextWaypoint);
    distanceToNextWaypoint *= -1.0;
    super.addTurnMoveCommandsToPathAndMoveCurrentPoint(distanceToNextWaypoint, angleToNextWaypoint);
  }

  private boolean robotSectionInFrontOfTurningCenter(RobotSectionsForWaypointsToMove robotSectionForWaypointToMove) {
    switch (robotSectionForWaypointToMove) {
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

  private boolean isNextWaypointSectionToMoveOnTurningCenter(WaypointWithRobotSectionToMove w) {
    return w.getRobotSectionToMove() == RobotSectionsForWaypointsToMove.CENTER;
  }

  private boolean isQuickerToMoveCenterOfRobotBackwards(double angleToNextWaypoint) {
    // if the angle to the next waypoint is more than 90˚ then it is is quicker to
    // turn to an angle less than 90˚ and move backwards. For example if the angle
    // to the next waypoint is 100˚ then it is quicker to just turn to an angle of
    // -80˚ and then drive backwards to the same point.
    return Math.abs(angleToNextWaypoint) > 90;
  }

  private Waypoint getNextWaypointForCenterOfRobot(WaypointWithRobotSectionToMove waypoint, double angle) {
    double distanceNextWayPointIsFromCenterOfRobot = getDistanceFromTurningCenterForRobotSection(
        waypoint.getRobotSectionToMove());
    Waypoint nextWaypointForCenterOfRobot = getNewWaypointMovedInDirection(waypoint,
        -distanceNextWayPointIsFromCenterOfRobot, angle);

    return nextWaypointForCenterOfRobot;

  }

  private double getDistanceFromTurningCenterForRobotSection(RobotSectionsForWaypointsToMove robotSection) {
    switch (robotSection) {
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
