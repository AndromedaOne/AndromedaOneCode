package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class DriveTrainDiagonalPathGenerator extends DiagonalPathGenerator {

  private DriveTrain m_driveTrain;
  private double m_maxOutput;

  public DriveTrainDiagonalPathGenerator(WaypointsBase waypoints, DriveTrain driveTrain, Waypoint initialWaypoint,
      double maxOutputs) {
    super(waypoints, initialWaypoint);
    m_maxOutput = maxOutputs;
    m_driveTrain = driveTrain;
  }

  public DriveTrainDiagonalPathGenerator(WaypointsBase waypoints, DriveTrain driveTrain, double maxOutput) {
    this(waypoints, driveTrain, new Waypoint(0, 0), maxOutput);
  }

  public DriveTrainDiagonalPathGenerator(WaypointsBase waypoints, DriveTrain driveTrain, Waypoint initialWaypoint) {
    this(waypoints, driveTrain, initialWaypoint, 0);
  }

  public DriveTrainDiagonalPathGenerator(WaypointsBase waypoints, DriveTrain driveTrain) {
    this(waypoints, driveTrain, new Waypoint(0, 0), 0);
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
    if (m_maxOutput != 0) {
      return new MoveUsingEncoder(m_driveTrain, distance, angle, m_maxOutput);
    }
    return new MoveUsingEncoder(m_driveTrain, distance, true, angle);
  }

}
