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

public class SimpleDriveTrainDiagonalPath extends SequentialCommandGroup {

  private class ExampleWayPoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      addWayPoint(new Waypoint(0, 0));
      addWayPoint(new Waypoint(0, 56));
      addWayPoint(new Waypoint(17.75, 56));
      addWayPoint(new Waypoint(-12.25, 56));
      addWayPoint(new Waypoint(15, -50));
      addWayPoint(new Waypoint(-12, -1));
      addWayPoint(new Waypoint(17.25, 56));
      addWayPoint(new Waypoint(0, 0));
    }
  }

  /** Creates a new RomiExamplePath. */
  public SimpleDriveTrainDiagonalPath(DriveTrainBase driveTrain) {
    PathGeneratorBase path = new DriveTrainDiagonalPathGenerator(getClass().getSimpleName(),
        new ExampleWayPoints(), driveTrain, 1.0, true, true);
    addCommands(path.getPath(), new TurnToCompassHeading(() -> 0));
  }

  public SimpleDriveTrainDiagonalPath() {
    addCommands();
  }
}
