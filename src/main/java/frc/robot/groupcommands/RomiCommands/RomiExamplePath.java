// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.groupcommands.RomiCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.pathgenerators.DriveTrainDiagonalPathGenerator;
import frc.robot.pathgeneration.pathgenerators.PathGeneratorBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class RomiExamplePath extends SequentialCommandGroup {

  private class ExampleWayPoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      addWayPoint(new Waypoint(0, 0));
      addWayPoint(new Waypoint(8, 2));
      addWayPoint(new Waypoint(-4, 20));
      addWayPoint(new Waypoint(12, -6));
      addWayPoint(new Waypoint(0, 0));
    }

  }

  /** Creates a new RomiExamplePath. */
  public RomiExamplePath(DriveTrain driveTrain) {
    PathGeneratorBase path = new DriveTrainDiagonalPathGenerator(new ExampleWayPoints(), driveTrain, new Waypoint(0, 0),
        0.75);
    addCommands(path.getPath());
  }
}
