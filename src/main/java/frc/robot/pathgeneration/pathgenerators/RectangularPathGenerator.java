package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.WayPointWithHeading;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public abstract class RectangularPathGenerator extends PathGeneratorBase {

  private double m_currentX;
  private double m_currentY;
  private SequentialCommandGroup m_path;
  private double initalWaypointDirection;

  public RectangularPathGenerator(WaypointsBase waypoints, WayPointWithHeading initialWaypoint) {
    super(waypoints, initialWaypoint);
    m_currentX = 0;
    m_currentY = 0;
    initalWaypointDirection = AngleConversionUtils.ConvertAngleToCompassHeading(initialWaypoint.getHeading());
    m_path = new SequentialCommandGroup();
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
    double deltaY = waypoint.getY() - m_currentY;
    double deltaX = waypoint.getX() - m_currentX;

    double RightAngleDegrees = AngleConversionUtils.ConvertAngleToCompassHeading(initalWaypointDirection + 90);

    m_path.addCommands(createTurnCommand(initalWaypointDirection), createMoveCommand(deltaY, initalWaypointDirection), createTurnCommand(RightAngleDegrees),
        createMoveCommand(deltaX, RightAngleDegrees));

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
