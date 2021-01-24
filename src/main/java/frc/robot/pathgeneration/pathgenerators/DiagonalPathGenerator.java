package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.AngleConversionUtils;

public abstract class DiagonalPathGenerator extends PathGeneratorBase {

  private Waypoint m_currentWaypointRelativeToInitalPoint;
  private SequentialCommandGroup m_path;

  public DiagonalPathGenerator(WaypointsBase waypoints, Waypoint initialWaypoint) {
    super(waypoints, initialWaypoint);

    m_currentWaypointRelativeToInitalPoint = new Waypoint(0, 0);
    m_path = new SequentialCommandGroup();
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint nextWaypointRelativeToInitialPoint) {
    double distance = m_currentWaypointRelativeToInitalPoint.distance(nextWaypointRelativeToInitialPoint);

    double compassAngle = getAngleToTurn(nextWaypointRelativeToInitialPoint);

    if (Math.abs(distance) > 0.001) {
      addTurnMoveCommands(distance, compassAngle);
    }

  }

  protected double getAngleToTurn(Waypoint waypoint) {
    double deltaX = waypoint.getX() - m_currentWaypointRelativeToInitalPoint.getX();
    double deltaY = waypoint.getY() - m_currentWaypointRelativeToInitalPoint.getY();
    double angleInDegreesCenteredAt0 = Math.toDegrees(Math.atan(deltaX / deltaY));
    if (deltaY < 0) {
      angleInDegreesCenteredAt0 += 180;
    }

    return AngleConversionUtils.ConvertAngleToCompassHeading(angleInDegreesCenteredAt0);
  }

  protected Waypoint getCurrentPointRelativeToInitialPoint() {
    return m_currentWaypointRelativeToInitalPoint;
  }

  protected void setCurrentWaypointRelativeToInitialPoint(Waypoint w) {
    m_currentWaypointRelativeToInitalPoint = w;
  }

  protected void addTurnMoveCommands(double distance, double angle) {
    addCommandToPath(createTurnCommand(angle));
    addCommandToPath(createMoveCommand(distance, angle));

    m_currentWaypointRelativeToInitalPoint = getNewWaypointMovedInDirection(getCurrentPointRelativeToInitialPoint(), distance, angle);
  }

  protected Waypoint getNewWaypointMovedInDirection(Waypoint waypoint, double distance, double direction) {
    // sin and cos are resversed in these calculations because it assumes direction
    // is being measured from North i.e. pointed in a direction of East would result
    // in a direction measurement of 90˚ rather than tha traditional polar
    // coordinate system which would assign East and angle of 0˚

    double newX = waypoint.getX() + distance * Math.sin(Math.toRadians(direction));
    double newY = waypoint.getY() + distance * Math.cos(Math.toRadians(direction));

    return new Waypoint(newX, newY);
  }

  protected abstract CommandBase createTurnCommand(double angle);

  protected abstract CommandBase createMoveCommand(double distance, double angle);

  private void addCommandToPath(CommandBase c) {
    m_path.addCommands(c);
  }

  @Override
  protected CommandBase getGeneratedPath() {
    return m_path;
  }

}
