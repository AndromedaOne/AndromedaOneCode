package frc.robot.pathgeneration.pathgenerators;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithHeading;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public abstract class PathGeneration2d extends PathGeneratorBase {

  private WaypointsBase m_waypoints;
  private WaypointWithHeading m_initialWaypoint;
  private WaypointWithHeading m_finalPoint;

  public PathGeneration2d(WaypointsBase waypoints, WaypointWithHeading initialWaypoint,
      WaypointWithHeading finalPoint) {
    super(waypoints, initialWaypoint);
    m_waypoints = waypoints;
    m_initialWaypoint = initialWaypoint;
    m_finalPoint = finalPoint;
  }

  @Override
  protected void generatePathForNextRelativeToStartWaypoint(Waypoint waypoint) {
    // this doesn't do anything because the path will be fully generated in
    // getGeneratedPath.
  }

  @Override
  protected CommandBase getGeneratedPath() {
    TrajectoryConfig config = getConfig();
    List<Translation2d> interiorPoints = new ArrayList<Translation2d>();
    for (Waypoint w : m_waypoints) {
      interiorPoints.add(convertInchesWaypointToMeterTranslation2d(w));
    }
    Pose2d initialPose2dPoint = convertInchesWaypointToMeterPose2d(m_initialWaypoint);
    Pose2d finalPose2dPoint = convertInchesWaypointToMeterPose2d(m_finalPoint);
    Trajectory trajectory = TrajectoryGenerator.generateTrajectory(initialPose2dPoint, interiorPoints, finalPose2dPoint,
        config);
    ResetOdometryCommand resetOdometryCommand = new ResetOdometryCommand(initialPose2dPoint);
    SequentialCommandGroup sequentialCommandGroup = new SequentialCommandGroup(resetOdometryCommand,
        getRamsete(trajectory));
    return sequentialCommandGroup;
  }

  private Translation2d convertInchesWaypointToMeterTranslation2d(Waypoint waypoint) {
    // x and y are reversed because thats what Translation2d expects.
    return new Translation2d(Units.inchesToMeters(waypoint.getY()), Units.inchesToMeters(waypoint.getX()));
  }

  private Pose2d convertInchesWaypointToMeterPose2d(WaypointWithHeading waypoint) {
    // x and y are reversed because thats what Pose2d expects.
    return new Pose2d(Units.inchesToMeters(waypoint.getY()), Units.inchesToMeters(waypoint.getX()),
        new Rotation2d(waypoint.getHeading()));
  }

  protected abstract RamseteCommand getRamsete(Trajectory trajectory);

  protected abstract TrajectoryConfig getConfig();

  protected abstract void resetOdometry(Pose2d pose);

  private class ResetOdometryCommand extends CommandBase {
    private Pose2d m_pose;

    public ResetOdometryCommand(Pose2d pose) {
      m_pose = pose;
    }

    @Override
    public void initialize() {
      resetOdometry(m_pose);
    }

    @Override
    public boolean isFinished() {
      return true;
    }
  }

}
