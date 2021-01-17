package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.WayPointWithHeading;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.AngleConversionUtils;

public abstract class RectangularPathGenerator extends PathGeneratorBase {

  private Waypoint m_currentWaypoint;
  private SequentialCommandGroup m_path;
  private double m_initalWaypointDirection;
  private double m_rightAngleDegrees;

  public RectangularPathGenerator(WaypointsBase waypoints, Waypoint initialWaypoint) {
    super(waypoints, initialWaypoint);
    m_path = new SequentialCommandGroup();
    m_currentWaypoint = initialWaypoint;
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
    double deltaY = waypoint.getY() - m_currentWaypoint.getY();
    double deltaX = waypoint.getX() - m_currentWaypoint.getX();

    m_path.addCommands(createTurnCommand(m_initalWaypointDirection),
        createMoveCommand(deltaY, m_initalWaypointDirection), createTurnCommand(m_rightAngleDegrees),
        createMoveCommand(deltaX, m_rightAngleDegrees));

    m_currentWaypoint = waypoint;
  }

  protected abstract CommandBase createTurnCommand(double angle);

  protected abstract CommandBase createMoveCommand(double distance, double angle);

  @Override
  protected CommandBase getGeneratedPath() {
    return m_path;
  }

}
