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

public class RomiChallenge2Course2 extends SequentialCommandGroup {
  private class Course2Waypoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {

      addWayPoint(new Waypoint(0, 8.5));
      addWayPoint(new Waypoint(-12.5, 22));
      addWayPoint(new Waypoint(-12, 55.5));
      addWayPoint(new Waypoint(4.5, 72.25));
      addWayPoint(new Waypoint(-16.5, 71.5));
      addWayPoint(new Waypoint(0.5, 35.25));
      addWayPoint(new Waypoint(-1.5, 22.5));
      addWayPoint(new Waypoint(-16.5, 3.25));
    }

  }

  /** Creates a new RomiChallenge2Course1. */
  public RomiChallenge2Course2(DriveTrain driveTrain) {
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
