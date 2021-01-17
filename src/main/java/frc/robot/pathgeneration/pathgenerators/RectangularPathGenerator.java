package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public abstract class RectangularPathGenerator extends PathGeneratorBase {

    private double m_currentX;
    private double m_currentY;
    private SequentialCommandGroup m_path;

    public RectangularPathGenerator(WaypointsBase waypoints, Waypoint initialWaypoint) {
        super(waypoints, initialWaypoint);
        m_currentX = 0;
        m_currentY = 0;
        m_path = new SequentialCommandGroup();
    }

    @Override
    protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
        double deltaY = waypoint.getY() - m_currentY;
        double deltaX = waypoint.getX() - m_currentX;

        m_path.addCommands(
            createTurnCommand(0),
            createMoveCommand(deltaY, 0),
            createTurnCommand(90),
            createMoveCommand(deltaX, 0)
        );

        m_currentX += deltaX;
        m_currentY += deltaY;
    }

    protected abstract CommandBase createTurnCommand(double angle);
    protected abstract CommandBase createMoveCommand(double distance, double angle);

    @Override
    protected CommandBase getGeneratedPath() {
        return m_path;
    }

    
    
}
