// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.romiCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.pathgenerators.DriveTrainRectangularPathGenerator;
import frc.robot.pathgeneration.pathgenerators.PathGeneratorBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.tankDriveTrain.TankDriveTrain;

public class AllianceAnticsSimple extends SequentialCommandGroup {
  private class Waypoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      addWayPoint(new Waypoint(0, 60));
      addWayPoint(new Waypoint(-15, 60));
      addWayPoint(new Waypoint(-15, 75));

    }

  }

  /** Creates a new AllianceAnticsSimple. */
  public AllianceAnticsSimple(TankDriveTrain driveTrain) {
    PathGeneratorBase path = new DriveTrainRectangularPathGenerator(getClass().getSimpleName(),
        new Waypoints(), driveTrain, 0.5);
    addCommands(path.getPath());
  }

}
