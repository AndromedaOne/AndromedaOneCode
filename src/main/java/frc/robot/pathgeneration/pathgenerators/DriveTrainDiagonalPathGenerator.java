package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.PauseRobot;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class DriveTrainDiagonalPathGenerator extends DiagonalPathGenerator {

  private DriveTrain m_driveTrain;
  private double m_maxOutput = 1.0;
  private boolean m_pauseAfterTurn = false;

  public DriveTrainDiagonalPathGenerator(String pathName, WaypointsBase waypoints,
      DriveTrain driveTrain, Waypoint initialWaypoint, double maxOutputs, boolean useReverse,
      boolean pauseAfterTurn) {
    super(pathName, waypoints, initialWaypoint, useReverse);
    m_maxOutput = maxOutputs;
    m_driveTrain = driveTrain;
    m_pauseAfterTurn = pauseAfterTurn;
  }

  // the first waypoint in the waypoints is the initial waypoint
  public DriveTrainDiagonalPathGenerator(String pathName, WaypointsBase waypoints,
      DriveTrain driveTrain, double maxOutputs, boolean useReverse, boolean pauseAfterTurn) {
    this(pathName, waypoints, driveTrain, new Waypoint(0, 0), maxOutputs, useReverse,
        pauseAfterTurn);
  }

  /**
   * @param angle is a compassheading x such that 0<= x < 360 degrees
   */
  @Override
  protected CommandBase createTurnCommand(double angle) {
    if (m_pauseAfterTurn) {
      return (new SequentialCommandGroup(new TurnToCompassHeading(angle),
          new PauseRobot(10, m_driveTrain)));
    }
    return (new TurnToCompassHeading(angle));
  }

  /**
   * @param angle    is a compassheading x such that 0<= x < 360 degrees
   * 
   * @param distance is a distance in inches
   */
  @Override
  protected CommandBase createMoveCommand(double distance, double angle) {
    return new MoveUsingEncoder(m_driveTrain, distance, angle, m_maxOutput);
  }

}
