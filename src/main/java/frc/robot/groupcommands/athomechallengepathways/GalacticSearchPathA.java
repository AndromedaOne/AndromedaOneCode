package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.Robot;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.pathgeneration.pathgenerators.DriveTrainDiagonalPathGenerator;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class GalacticSearchPathA extends CommandBase {
  private final double robotLength = 40;
  private final Waypoint startPoint = AtHomeChallengePoints.C2.subtract(new Waypoint(0, robotLength / 2));
  private final double maxSpeed = 0.5;

  private class BluePath extends WaypointsBase {
    // Waypoints for the blue path
    @Override
    protected void loadWaypoints() {
      addWayPoint(AtHomeChallengePoints.E6);
      addWayPoint(AtHomeChallengePoints.B7);
      addWayPoint(AtHomeChallengePoints.C9);
      addWayPoint(AtHomeChallengePoints.C11);
    }

  }

  private class RedPath extends WaypointsBase {
    // Waypoints for the red path
    @Override
    protected void loadWaypoints() {
      addWayPoint(AtHomeChallengePoints.C3);
      addWayPoint(AtHomeChallengePoints.D5);
      addWayPoint(AtHomeChallengePoints.A6);
      addWayPoint(AtHomeChallengePoints.A11);
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    final double distanceToPowercell = Robot.getInstance().getSensorsContainer().getPowercellDetector()
        .getDistanceInches();
    if (distanceToPowercell <= 30) {
      DriveTrainDiagonalPathGenerator red = new DriveTrainDiagonalPathGenerator(new RedPath(),
          Robot.getInstance().getSubsystemsContainer().getDrivetrain(), startPoint, maxSpeed);
      CommandBase redPath = red.getPath();
      CommandBase cmd = new ParallelDeadlineGroup(redPath,
          new DeployAndRunIntake(Robot.getInstance().getSubsystemsContainer().getIntake(), () -> false));
      CommandScheduler.getInstance().schedule(cmd);
    } else {
      DriveTrainDiagonalPathGenerator blue = new DriveTrainDiagonalPathGenerator(new BluePath(),
          Robot.getInstance().getSubsystemsContainer().getDrivetrain(), startPoint, maxSpeed);
      CommandBase bluePath = blue.getPath();
      CommandBase cmdblue = new ParallelDeadlineGroup(bluePath,
          new DeployAndRunIntake(Robot.getInstance().getSubsystemsContainer().getIntake(), () -> false));
      CommandScheduler.getInstance().schedule(cmdblue);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
