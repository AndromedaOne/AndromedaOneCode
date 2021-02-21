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

/** Add your docs here. */
public class RomiChallenge1 extends SequentialCommandGroup {
  private class ChallengePath extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      addWayPoint(new Waypoint(0,0));
      addWayPoint(new Waypoint(0,18.0));   // 0,18.5
      addWayPoint(new Waypoint(-18,18.5));
      addWayPoint(new Waypoint(-7,-8));  // -8,-8 
      addWayPoint(new Waypoint(-25.75,-7.25));  // -26, -8
      addWayPoint(new Waypoint(-27,10));
  
    }
  }

  public RomiChallenge1(DriveTrain drivetrain) {
    PathGeneratorBase generaterbase = new DriveTrainDiagonalPathGenerator(new ChallengePath(), drivetrain,
        //new Waypoint(0, 0));  // 0, 0
        new Waypoint(0,0), 0.50);  // was 0.75
    addCommands(generaterbase.getPath());
  }
}
