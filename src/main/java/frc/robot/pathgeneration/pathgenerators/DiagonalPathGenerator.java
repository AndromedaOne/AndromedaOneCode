package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;


public abstract class DiagonalPathGenerator extends PathGeneratorBase {

    private Waypoint m_currentWaypoint;
    private Waypoint m_initialWaypoint;
    private SequentialCommandGroup m_path;

    public DiagonalPathGenerator(WaypointsBase waypoints, Waypoint initialWaypoint) {
        super(waypoints);

        m_currentWaypoint = initialWaypoint;
        m_initialWaypoint = initialWaypoint;
        m_path = new SequentialCommandGroup();
    }

    public DiagonalPathGenerator(WaypointsBase waypoints) {
        this(waypoints, new Waypoint(0, 0, 0));
    }

    @Override
    protected void generatePathForNextWaypoint(Waypoint waypoint) {

        double distance = m_currentWaypoint.distance(waypoint);

        double deltaX = waypoint.getX() - m_currentWaypoint.getX();
        double deltaY = waypoint.getY() - m_currentWaypoint.getY();

        double angleInDegreesCenteredAt0 = Math.toDegrees(Math.atan2(deltaY, deltaX));
        double angleInDegreesRelativeToStart = angleInDegreesCenteredAt0 - m_initialWaypoint.getHeading();

        double compassAngleRelativeToStart = getCompassHeadingFromAngle(angleInDegreesRelativeToStart);

        m_path.addCommands(
            createTurnCommand(compassAngleRelativeToStart),
            createMoveCommand(distance, compassAngleRelativeToStart));
    
        m_currentWaypoint = waypoint;
    }

    private double getCompassHeadingFromAngle(double angle) {
        
        return 0;
    }

    protected abstract CommandBase createTurnCommand(double angle);
    protected abstract CommandBase createMoveCommand(double distance, double angle);

    @Override
    protected CommandBase getGeneratedPath() {
        // TODO Auto-generated method stub
        return m_path;
    }
    
}
