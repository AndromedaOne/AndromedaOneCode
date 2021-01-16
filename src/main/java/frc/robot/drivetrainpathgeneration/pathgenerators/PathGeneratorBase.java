package frc.robot.drivetrainpathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.drivetrainpathgeneration.waypoints.Waypoint;
import frc.robot.drivetrainpathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public abstract class PathGeneratorBase {

    private WaypointsBase m_waypoints;
    private DriveTrain m_drivetrain;
    private CommandBase m_path;

    public PathGeneratorBase(DriveTrain driveTrain, WaypointsBase waypoints) {
        m_waypoints = waypoints;
        m_drivetrain = driveTrain;
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

    protected DriveTrain getDriveTrain() {
        return m_drivetrain;
    }
}
