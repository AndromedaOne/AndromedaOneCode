package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.pathgeneration.pathgenerators.DiagonalPathGenerator;
import frc.robot.pathgeneration.pathgenerators.DriveTrainDiagonalPathGenerator;
import frc.robot.pathgeneration.pathgenerators.PathGeneratorBase;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.intake.IntakeBase;

public class GalacticSearchPathA extends SequentialCommandGroup {
  private DriveTrain m_driveTrain;
  private class GalacticSearchWaypoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      addWayPoint(AtHomeChallengePoints.B1.average(AtHomeChallengePoints.D1));
      addWayPoint(AtHomeChallengePoints.C3);
      addWayPoint(AtHomeChallengePoints.D5);
      addWayPoint(AtHomeChallengePoints.E6);
      addWayPoint(AtHomeChallengePoints.A6);
      addWayPoint(AtHomeChallengePoints.B7);
      addWayPoint(AtHomeChallengePoints.C9);
      addWayPoint(AtHomeChallengePoints.B11.average(AtHomeChallengePoints.D11));
    }

  }
  

  public GalacticSearchPathA(DriveTrain driveTrain, IntakeBase intake) {
    m_driveTrain = driveTrain;
    Waypoint initialPoint = AtHomeChallengePoints.B1.average(AtHomeChallengePoints.D1);
    PathGeneratorBase diagonalPathGenerator = new DriveTrainDiagonalPathGenerator(new GalacticSearchWaypoints(), driveTrain,  initialPoint);
    addCommands(new ParallelDeadlineGroup(diagonalPathGenerator.getPath(), new DeployAndRunIntake(intake, () -> false)));
  }

}
