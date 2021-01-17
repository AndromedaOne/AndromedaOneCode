package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;


public abstract class DiagonalPathGenerator extends PathGeneratorBase {

    private Waypoint m_currentWaypoint;
    private SequentialCommandGroup m_path;

    public DiagonalPathGenerator(WaypointsBase waypoints) {
        super(waypoints);

        m_currentWaypoint = new Waypoint(0, 0);
        m_path = new SequentialCommandGroup();
    }

    @Override
    protected void generatePathForNextWaypoint(Waypoint waypoint) {

        double distance = m_currentWaypoint.distance(waypoint);
        double deltaX = waypoint.getX() - m_currentWaypoint.getX();
        double deltaY = waypoint.getY() - m_currentWaypoint.getY();
        double angleInRadiansCenteredAt0 = Math.atan2(deltaY, deltaX);
        double angleInRadiansCenteredAtPi = angleInRadiansCenteredAt0;
        
        if(angleInRadiansCenteredAtPi < 0) {
            angleInRadiansCenteredAtPi += Math.PI * 2;
        }

        double compassAngleRelativeToStart = Math.toDegrees(angleInRadiansCenteredAtPi);
        
        // This is here to account for rounding errors when using math functions.
        // It ensures that compassAngleRelativeToStart is greater than 0 (inclusive) and
        // less than 360 (exclusive)
        if(compassAngleRelativeToStart >= 360) {
            compassAngleRelativeToStart -= 360;
        }else if(compassAngleRelativeToStart < 0) {
            compassAngleRelativeToStart += 360;
        }

        m_path.addCommands(
            createTurnCommand(compassAngleRelativeToStart),
            createMoveCommand(distance, compassAngleRelativeToStart));
    
        m_currentWaypoint = waypoint;
    }

    protected abstract CommandBase createTurnCommand(double angle);
    protected abstract CommandBase createMoveCommand(double distance, double angle);

    @Override
    protected CommandBase getGeneratedPath() {
        // TODO Auto-generated method stub
        return m_path;
    }
    
}
