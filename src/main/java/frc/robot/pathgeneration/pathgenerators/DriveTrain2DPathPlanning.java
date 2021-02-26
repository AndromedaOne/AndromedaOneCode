package frc.robot.pathgeneration.pathgenerators;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class DriveTrain2DPathPlanning extends PathGeneratorBase {

  public DriveTrain2DPathPlanning(WaypointsBase waypoints, Config driveTrainConfig) {
    super(waypoints);
  }

  @Override
  protected void generatePathForNextWaypoint(Waypoint waypoint) {

  }

  @Override
  protected CommandBase getGeneratedPath() {
    // TODO Auto-generated method stub
    return null;
  }

}
