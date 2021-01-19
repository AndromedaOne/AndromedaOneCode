package frc.robot.pathgeneration.pathgenerators;

import java.security.InvalidParameterException;

import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithRobotSide;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.RobotDimensions;

public abstract class DPGAdjustingForRobotSides extends DiagonalPathGenerator {

    public DPGAdjustingForRobotSides(WaypointsBase waypoints, Waypoint initialWaypoint) {
        super(waypoints, initialWaypoint);
        if(initialWaypoint instanceof WaypointWithRobotSide) {
            throw new InvalidParameterException("When giving a waypoint with robot side you must include an intial heading!");
        }
    }

    public DPGAdjustingForRobotSides(WaypointsBase waypoints, WaypointWithRobotSide initialWaypoint, double intialHeading) {
        super(waypoints, initialWaypoint);
        
        Waypoint initialWaypointAdjustedForRobotSide = getWaypointAdjustedForRobotSide(initialWaypoint, intialHeading);

        super.setInitialPoint(initialWaypointAdjustedForRobotSide);
    }

    @Override
    public void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
        double angle = getAngleToTurn(waypoint);
        Waypoint adjustedWaypoint = getWaypointAdjustedForRobotSide(waypoint, angle);
        super.generatePathForNextRelativeToStartWaypoint(adjustedWaypoint);
    }

    private Waypoint getWaypointAdjustedForRobotSide(Waypoint waypoint, double angle) {
        Waypoint adjustedWaypoint = waypoint.copy();
        if(waypoint instanceof WaypointWithRobotSide) {
            WaypointWithRobotSide waypointWithRobotSide = (WaypointWithRobotSide) adjustedWaypoint;
            double distance = 0;
            switch(waypointWithRobotSide.getSide()){
                case CENTER:
                    break;
                case BACKOFBUMPER:
                    distance += RobotDimensions.getInstance().getLength() *  0.5 + RobotDimensions.getInstance().getBumperThickness();
                    adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, distance ,angle);
                    break;
                case BACKOFFRAME:
                    distance += RobotDimensions.getInstance().getLength() *  0.5;
                    adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, distance ,angle);
                    break;
                case FRONTOFFRAME:
                    distance -= RobotDimensions.getInstance().getLength() *  0.5;
                    adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, distance ,angle);
                    break;
                case FRONTOFBUMPER:
                    distance -= (RobotDimensions.getInstance().getLength() *  0.5 + RobotDimensions.getInstance().getBumperThickness());
                    adjustedWaypoint = getNewWaypointMovedInDirection(waypointWithRobotSide, distance ,angle);
                    break;
            }
        }

        return adjustedWaypoint;

    }

    private Waypoint getNewWaypointMovedInDirection(Waypoint waypoint, double distance, double direction) {
        // sin and cos are resversed in these calculations because it assumes direction is being measured from North i.e. pointed in a direction of East would result in a direction measurement of 90˚ rather than tha traditional polar coordinate system which would assign East and angle of 0˚
        
        double newX = waypoint.getX() + distance * Math.sin(Math.toRadians(direction));
        double newY = waypoint.getY() + distance * Math.cos(Math.toRadians(direction));

        return new Waypoint(newX, newY);
    }
    
}
