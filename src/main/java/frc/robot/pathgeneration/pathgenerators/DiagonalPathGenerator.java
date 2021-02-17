package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.AngleConversionUtils;

public abstract class DiagonalPathGenerator extends PathGeneratorBase {

  private Waypoint m_currentWaypoint;
  private SequentialCommandGroup m_path;
  private boolean m_useReverse = false;
  private double m_previousCompassHeading;

  private enum RobotDirection {
    forward, reverse
  }

  private RobotDirection m_lastDirection = RobotDirection.forward;

   

  public DiagonalPathGenerator(WaypointsBase waypoints, Waypoint initialWaypoint, boolean useReverse) {
    super(waypoints, initialWaypoint);
    m_useReverse = useReverse;
    m_currentWaypoint = new Waypoint(0, 0);
    m_path = new SequentialCommandGroup();
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {

    double distance = m_currentWaypoint.distance(waypoint);

    double deltaX = waypoint.getX() - m_currentWaypoint.getX();
    double deltaY = waypoint.getY() - m_currentWaypoint.getY();
    double angleInDegreesCenteredAt0 = Math.toDegrees(Math.atan(deltaX / deltaY));

    if (deltaY < 0) {
      angleInDegreesCenteredAt0 += 180;
    }
    double compassAngle = AngleConversionUtils.ConvertAngleToCompassHeading(angleInDegreesCenteredAt0);
    if (m_useReverse && (Math.abs(compassAngle - m_previousCompassHeading) > 90)) {
      compassAngle = 360 - compassAngle;
      if (m_lastDirection == RobotDirection.forward) {
        distance = -distance;
      }
      m_previousCompassHeading = compassAngle;
    }
    if (distance != 0) {
      m_path.addCommands(createTurnCommand(compassAngle));
      m_path.addCommands(createMoveCommand(distance, compassAngle));
    }
    m_currentWaypoint = waypoint;
  }

  protected abstract CommandBase createTurnCommand(double angle);

  protected abstract CommandBase createMoveCommand(double distance, double angle);

  @Override
  protected CommandBase getGeneratedPath() {
    return m_path;
  }

}
