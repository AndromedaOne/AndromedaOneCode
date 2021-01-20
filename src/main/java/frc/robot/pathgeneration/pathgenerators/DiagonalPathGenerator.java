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

    m_currentWaypoint = new Waypoint(0, 0);
    m_path = new SequentialCommandGroup();
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {

    double distance = m_currentWaypoint.distance(waypoint);

    double compassAngle = getAngleToTurn(waypoint);

    if (distance != 0) {
      m_path.addCommands(createTurnCommand(compassAngle));
      m_path.addCommands(createMoveCommand(distance, compassAngle));
    }
    m_currentWaypoint = waypoint;
  }

  protected double getAngleToTurn(Waypoint waypoint) {
    double deltaX = waypoint.getX() - m_currentWaypoint.getX();
    double deltaY = waypoint.getY() - m_currentWaypoint.getY();
    double angleInDegreesCenteredAt0 = Math.toDegrees(Math.atan(deltaX / deltaY));

    if (deltaY < 0) {
      angleInDegreesCenteredAt0 += 180;
    }

    return AngleConversionUtils.ConvertAngleToCompassHeading(angleInDegreesCenteredAt0);
  }

  protected Waypoint getCurrentPoint() {
    return m_currentWaypoint;
  }

  protected void setCurrentWaypoint(Waypoint w) {
    m_currentWaypoint = w;
  }

  protected abstract CommandBase createTurnCommand(double angle);

  protected abstract CommandBase createMoveCommand(double distance, double angle);

  protected void addCommandToPath(CommandBase c) {
    m_path.addCommands(c);
  }

  @Override
  protected CommandBase getGeneratedPath() {
    return m_path;
  }

}
