// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.examplePathCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.pathgeneration.pathgenerators.DriveTrainDiagonalPathGenerator;
import frc.robot.pathgeneration.pathgenerators.PathGeneratorBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class DriveTrainDiagonalPath extends SequentialCommandGroup {

  private class ExampleWayPoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      addWayPoint(new Waypoint(0, 0));
      addWayPoint(new Waypoint(0, 8));
      addWayPoint(new Waypoint(8, 8));
      addWayPoint(new Waypoint(8, 0));
      addWayPoint(new Waypoint(0, 0));
      addWayPoint(new Waypoint(5, 8.2));
      addWayPoint(new Waypoint(-6.8, -1.7));
      addWayPoint(new Waypoint(8.34, -7.3));
      addWayPoint(new Waypoint(-4.74, 6.35));
      addWayPoint(new Waypoint(0, 0));
    }

  }

  /** Creates a new RomiExamplePath. */
  public DriveTrainDiagonalPath(DriveTrainBase driveTrain) {
    PathGeneratorBase path = new DriveTrainDiagonalPathGenerator(getClass().getSimpleName(),
        new ExampleWayPoints(), driveTrain, 0.5, true, true);
    addCommands(path.getPath(), new TurnToCompassHeading(0));
  }
}
