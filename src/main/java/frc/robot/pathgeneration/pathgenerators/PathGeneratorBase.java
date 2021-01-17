package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public abstract class PathGeneratorBase {

    private WaypointsBase m_waypoints;
    private Waypoint m_initialPoint;

    public PathGeneratorBase(WaypointsBase waypoints, Waypoint initialPoint) {
        m_waypoints = waypoints;
        m_initialPoint = initialPoint;
    }

    public CommandBase getPath() {
        iterateThroughWaypointsToGeneratePath();
        return getGeneratedPath();
    }

    private void iterateThroughWaypointsToGeneratePath() {
        for(Waypoint w : m_waypoints) {
            generatePathForNextRelativeToStartWaypoint(w.subtract(m_initialPoint));
        }
    } 

    protected abstract void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint);
    protected abstract CommandBase getGeneratedPath();

}
