package frc.robot.drivetrainpathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.drivetrainpathgeneration.waypoints.Waypoint;
import frc.robot.drivetrainpathgeneration.waypoints.Waypoints;
import frc.robot.subsystems.drivetrain.DriveTrain;

public abstract class PathGeneratorBase {

    private Waypoints m_waypoints;
    private DriveTrain m_drivetrain;

    public PathGeneratorBase(DriveTrain driveTrain, Waypoints waypoints) {
        m_waypoints = waypoints;
        m_drivetrain = driveTrain;
    }

    public abstract CommandBase generatePath();
    
    protected Waypoint getNextWaypointpoint() {
        return m_waypoints.getNextWaypoint();
    }

    protected boolean hasNextWaypoint() {
        return m_waypoints.hasNextWaypoint();
    }

    protected DriveTrain getDriveTrain() {
        return m_drivetrain;
    }
}
