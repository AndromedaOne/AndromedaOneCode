package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public abstract class RectangularPathGenerator extends PathGeneratorBase {

  private Waypoint m_currentWaypoint;
  private SequentialCommandGroup m_path;

  public RectangularPathGenerator(WaypointsBase waypoints, Waypoint initialWaypoint) {
    super(waypoints, initialWaypoint);
    m_path = new SequentialCommandGroup();
    m_currentWaypoint = new Waypoint(0, 0);
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
    double deltaY = waypoint.getY() - m_currentWaypoint.getY();
    double deltaX = waypoint.getX() - m_currentWaypoint.getX();

    if(deltaY > 0){
      m_path.addCommands(createTurnCommand(0), createMoveCommand(deltaY, 0));
    }else if(deltaY < 0) {
      m_path.addCommands(createTurnCommand(180), createMoveCommand(Math.abs(deltaY), 180));
    }
    if(deltaX > 0) {
      m_path.addCommands(createTurnCommand(90), createMoveCommand(deltaX, 90));
    }else if(deltaX < 0) {
      m_path.addCommands(createTurnCommand(270), createMoveCommand(Math.abs(deltaX), 270));
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
