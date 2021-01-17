package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.AngleConversionUtils;

public abstract class DiagonalPathGenerator extends PathGeneratorBase {

  private Waypoint m_currentWaypoint;
  private SequentialCommandGroup m_path;

  public DiagonalPathGenerator(WaypointsBase waypoints, Waypoint initialWaypoint) {
    super(waypoints, initialWaypoint);

    m_currentWaypoint = initialWaypoint;
    m_path = new SequentialCommandGroup();
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {

    double distance = m_currentWaypoint.distance(waypoint);

    double deltaX = waypoint.getX() - m_currentWaypoint.getX();
    double deltaY = waypoint.getY() - m_currentWaypoint.getY();

    double angleInDegreesCenteredAt0 = Math.toDegrees(Math.atan2(deltaY, deltaX));

    double compassAngle = AngleConversionUtils.ConvertAngleToCompassHeading(angleInDegreesCenteredAt0);

    m_path.addCommands(createTurnCommand(compassAngle), createMoveCommand(distance, compassAngle));

    m_currentWaypoint = waypoint;
  }

  protected abstract CommandBase createTurnCommand(double angle);

  protected abstract CommandBase createMoveCommand(double distance, double angle);

  @Override
  protected CommandBase getGeneratedPath() {
    return m_path;
  }

}
