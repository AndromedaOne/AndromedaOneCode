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
import frc.robot.telemetries.Trace;

public class RomiChallenge2Course1 extends SequentialCommandGroup {
  private class Course2Waypoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      addWayPoint(new Waypoint(0, 41));
      addWayPoint(new Waypoint(15, 30));
      addWayPoint(new Waypoint(0, 17.5));
      addWayPoint(new Waypoint(0, 72.5));
      addWayPoint(new Waypoint(-15, 53.5));
      addWayPoint(new Waypoint(-7, 43.5));
      addWayPoint(new Waypoint(16, 67));
      addWayPoint(new Waypoint(0, 77));
      addWayPoint(new Waypoint(0, 0));
    }

  }

  /** Creates a new RomiChallenge2Course1. */
  public RomiChallenge2Course1(DriveTrain driveTrain) {
    PathGeneratorBase path = new DriveTrainDiagonalPathGenerator(getClass().getSimpleName(), new Course2Waypoints(),
        driveTrain, new Waypoint(0, 0), 0.6, true, true);
    addCommands(path.getPath());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }
}
