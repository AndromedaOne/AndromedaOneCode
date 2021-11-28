package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class DriveTrainRectangularPathGenerator extends RectangularPathGenerator {

  private DriveTrain m_driveTrain;
  private double m_maxMoveOutput;

  public DriveTrainRectangularPathGenerator(String pathName, WaypointsBase waypoints, DriveTrain driveTrain,
      Waypoint initialWaypoint, double maxMoveOutput) {
    super(pathName, waypoints, initialWaypoint);
    m_driveTrain = driveTrain;
    m_maxMoveOutput = maxMoveOutput;
  }

  public DriveTrainRectangularPathGenerator(String pathName, WaypointsBase waypoints, DriveTrain driveTrain) {
    this(pathName, waypoints, driveTrain, new Waypoint(0, 0), 1.0);
  }

  public DriveTrainRectangularPathGenerator(String pathName, WaypointsBase waypoints, DriveTrain driveTrain,
      double maxMoveOutput) {
    this(pathName, waypoints, driveTrain, new Waypoint(0, 0), maxMoveOutput);
  }

  /**
   * @param angle is a compassheading x such that 0<= x < 360 degrees
   */
  @Override
  protected CommandBase createTurnCommand(double angle) {
    return new TurnToCompassHeading(angle);
  }

  /**
   * @param angle    is a compassheading x such that 0<= x < 360 degrees
   * 
   * @param distance is a distance in inches
   */
  @Override
  protected CommandBase createMoveCommand(double distance, double angle) {
    return new MoveUsingEncoder(m_driveTrain, distance, angle, m_maxMoveOutput);
  }

}
