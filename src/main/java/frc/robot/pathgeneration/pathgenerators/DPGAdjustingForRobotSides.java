package frc.robot.pathgeneration.pathgenerators;

import java.security.InvalidParameterException;

import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithRobotSide;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.AngleConversionUtils;
import frc.robot.utils.RobotDimensions;
import frc.robot.utils.RobotSections;

public abstract class DPGAdjustingForRobotSides extends DiagonalPathGenerator {
    private RobotDimensions m_robotDimensions;

    public DPGAdjustingForRobotSides(WaypointsBase waypoints, Waypoint initialWaypoint, double initialHeading, RobotDimensions robotDimensions) {
        super(waypoints, initialWaypoint);
        m_robotDimensions = robotDimensions;
        Waypoint initialWaypointAdjustedForRobotSide = getWaypointAdjustedForRobotSide(initialWaypoint, initialHeading);

        super.setInitialPoint(initialWaypointAdjustedForRobotSide);
        
    }

    public DPGAdjustingForRobotSides(WaypointsBase waypoints, Waypoint initialWaypoint,  RobotDimensions robotDimensions) {
        this(waypoints, initialWaypoint, Double.NaN, robotDimensions);
        if(initialWaypoint instanceof WaypointWithRobotSide) {
            throw new InvalidParameterException("When giving a waypoint with robot side you must include the initial heading of the robot!");
        }
        
    }

    @Override
    public void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
        double angle = getAngleToTurn(waypoint);

        // Only occurs when the robot does not have to move at all.
        if(Double.isNaN(angle)) {
            return;
        }
        if(waypoint instanceof WaypointWithRobotSide) {
            WaypointWithRobotSide waypointWithRobotSide = (WaypointWithRobotSide) waypoint;
            if(isWaypointWithinTurningRadius(waypoint)){
                addCommandsForPointWithinTurningRadiusAndSetCurrentWaypoint(waypointWithRobotSide, angle);
                return;
            }
        }
        Waypoint adjustedWaypoint = getWaypointAdjustedForRobotSide(waypoint, angle);
        super.generatePathForNextRelativeToStartWaypoint(adjustedWaypoint);
    }

    private boolean isWaypointWithinTurningRadius(Waypoint waypoint) {
        if(waypoint instanceof WaypointWithRobotSide) {
            WaypointWithRobotSide waypointWithRobotSide = (WaypointWithRobotSide) waypoint;
            double distanceAdjustmentFromRobotSection = getDistanceAdjustmentFromRobotSection(waypointWithRobotSide.getSection());
            return waypoint.distance(getCurrentPoint()) <= distanceAdjustmentFromRobotSection;
        }
        return false;
    }

    private void addCommandsForPointWithinTurningRadiusAndSetCurrentWaypoint(WaypointWithRobotSide waypoint, double angle) {
        double distanceAdjustmentFromRobotSection = getDistanceAdjustmentFromRobotSection(waypoint.getSection());
        double distance = getCurrentPoint().distance(waypoint); 
        if(robotSectionAtFrontOfRobot(waypoint.getSection())){
            createTurnCommand(angle);
            distance -= distanceAdjustmentFromRobotSection;
            createMoveCommand(distance, angle);
        }else {
            angle = AngleConversionUtils.ConvertAngleToCompassHeading(angle + 180);
            createTurnCommand(angle);
            distance = distanceAdjustmentFromRobotSection - distance;
            createMoveCommand(distance, angle);
        }
        setCurrentWaypoint(getNewWaypointMovedInDirection(getCurrentPoint(), distance, angle));
    }

    private boolean robotSectionAtFrontOfRobot(RobotSections robotSections) {
        switch(robotSections) {
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

    private Waypoint getWaypointAdjustedForRobotSide(Waypoint waypoint, double angle) {
        Waypoint adjustedWaypoint = waypoint.copy();
        if(waypoint instanceof WaypointWithRobotSide) {
            WaypointWithRobotSide waypointWithRobotSide = (WaypointWithRobotSide) waypoint;
            double distance = getDistanceAdjustmentFromRobotSection(waypointWithRobotSide.getSection());
            if(robotSectionAtFrontOfRobot(waypointWithRobotSide.getSection())){
                adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, -distance, angle);
            }else {
                adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, distance, angle);
            }
        }

        return adjustedWaypoint;

    }

    private double getDistanceAdjustmentFromRobotSection(RobotSections robotSections) {
        switch(robotSections) {
            case CENTER:
                return 0;
            case BACKOFBUMPER:
                return m_robotDimensions.getLength() *  0.5 + m_robotDimensions.getBumperThickness();
            case BACKOFFRAME:
                return m_robotDimensions.getLength() *  0.5;
            case FRONTOFFRAME:
                return m_robotDimensions.getLength() *  0.5;
            case FRONTOFBUMPER:
                return m_robotDimensions.getLength() *  0.5 + m_robotDimensions.getBumperThickness();
        }
        return 0;
    }

    private Waypoint getNewWaypointMovedInDirection(Waypoint waypoint, double distance, double direction) {
        // sin and cos are resversed in these calculations because it assumes direction is being measured from North i.e. pointed in a direction of East would result in a direction measurement of 90˚ rather than tha traditional polar coordinate system which would assign East and angle of 0˚
        
        double newX = waypoint.getX() + distance * Math.sin(Math.toRadians(direction));
        double newY = waypoint.getY() + distance * Math.cos(Math.toRadians(direction));

        return new Waypoint(newX, newY);
    }
    
}
