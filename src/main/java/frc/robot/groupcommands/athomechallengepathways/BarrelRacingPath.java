package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.pathgenerators.DriveTrainDiagonalPathGenerator;
import frc.robot.pathgeneration.pathgenerators.PathGeneratorBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class BarrelRacingPath extends SequentialCommandGroup {
  private static double maximumPower = 0.5;
  private static double robotLengthInches = 40;
  private static Waypoint initialPoint = AtHomeChallengePoints.C2.subtract(new Waypoint(0, robotLengthInches / 2));

  private class BarrelWaypoints extends WaypointsBase {
    @Override
    protected void loadWaypoints() {
      addWayPoint(initialPoint);
      addWayPoint(AtHomeChallengePoints.C6);
      addWayPoint(AtHomeChallengePoints.E6);
      addWayPoint(AtHomeChallengePoints.E4);
      addWayPoint(AtHomeChallengePoints.C4);
      addWayPoint(AtHomeChallengePoints.C9);
      addWayPoint(AtHomeChallengePoints.A9);
      addWayPoint(AtHomeChallengePoints.A7);
      addWayPoint(AtHomeChallengePoints.E7);
      addWayPoint(AtHomeChallengePoints.E11);
      addWayPoint(AtHomeChallengePoints.C11);
      addWayPoint(AtHomeChallengePoints.C2);
    }
  }

  public BarrelRacingPath(DriveTrain driveTrain) {
    PathGeneratorBase pathGenerator = new DriveTrainDiagonalPathGenerator(new BarrelWaypoints(), driveTrain,
        initialPoint, maximumPower);
    addCommands(pathGenerator.getPath());
  }
}