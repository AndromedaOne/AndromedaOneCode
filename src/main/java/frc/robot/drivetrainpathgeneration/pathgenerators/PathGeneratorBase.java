package frc.robot.drivetrainpathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.drivetrainpathgeneration.waypoints.Waypoint;
import frc.robot.drivetrainpathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public abstract class PathGeneratorBase {

    private WaypointsBase m_waypoints;

    public PathGeneratorBase(WaypointsBase waypoints) {
        m_waypoints = waypoints;
    }

    public CommandBase getPath() {
        iterateThroughWaypointsToGeneratePath();
        return getGeneratedPath();
    }

    private void iterateThroughWaypointsToGeneratePath() {
        for(Waypoint w : m_waypoints) {
            generatePathForNextWaypoint(w);
        }
    } 

    protected abstract void generatePathForNextWaypoint(Waypoint waypoint);
    protected abstract CommandBase getGeneratedPath();

}
