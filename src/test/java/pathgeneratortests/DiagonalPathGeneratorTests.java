package pathgeneratortests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import dummycommands.DummyMoveCommand;
import dummycommands.DummyPathChecker;
import dummycommands.DummyTurnCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.groupcommands.athomechallengepathways.AtHomeChallengePoints;
import frc.robot.pathgeneration.pathgenerators.DiagonalPathGenerator;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.Waypoints;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class DiagonalPathGeneratorTests {

  private class DiagonalPathGeneratorTester extends DiagonalPathGenerator {

    public DiagonalPathGeneratorTester(WaypointsBase waypoints, Waypoint initialWaypoint) {
      super(waypoints, initialWaypoint);
    }

    @Override
    protected CommandBase createTurnCommand(double angle) {
      DummyTurnCommand dummyTurnCommand = new DummyTurnCommand(angle);
      return dummyTurnCommand;
    }

    @Override
    protected CommandBase createMoveCommand(double distance, double angle) {
      DummyMoveCommand dummyMoveCommand = new DummyMoveCommand(distance, angle);
      return dummyMoveCommand;
    }

  }

  private void createSimpleDiagonalPathGeneratorTest(Waypoint[] waypoints, Waypoint initialPoint,
      SequentialCommandGroup solution) {
    Waypoints testPoints = new Waypoints(waypoints);

    DiagonalPathGeneratorTester diagonalPathGeneratorTester = new DiagonalPathGeneratorTester(testPoints, initialPoint);

    CommandBase generatedOutput = diagonalPathGeneratorTester.getPath();

    DummyPathChecker.checkDummyCommandGroupsAreEquivalent(solution, generatedOutput);

  }

  @Test
  public void twoPointsVerticallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(0, 5) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(0), new DummyMoveCommand(5, 0));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(5, 0) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(90), new DummyMoveCommand(5, 90));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAllignedTurnLeft() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(-5, 0) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(270),
        new DummyMoveCommand(5, 270));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAllignedTurnAround() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(0, -4) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(180),
        new DummyMoveCommand(4, 180));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsDiagonalFromEachOther() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(2, 2) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(45),
        new DummyMoveCommand(Math.sqrt(8), 45));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsDiagonalFromEachOther2() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(2, -2) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(135),
        new DummyMoveCommand(Math.sqrt(8), 135));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void newInitialPosition1() {

    Waypoint initialPoint = new Waypoint(5, 5);
    Waypoint[] waypoints = { new Waypoint(7, 7) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(45),
        new DummyMoveCommand(Math.sqrt(8), 45));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void newInitialPosition2() {

    Waypoint initialPoint = new Waypoint(1, 4);
    Waypoint[] waypoints = { new Waypoint(4, 8) };
    double angleToTurnTo = Math.toDegrees(Math.atan(3.0 / 4.0));
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(angleToTurnTo),
        new DummyMoveCommand(5, angleToTurnTo));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void multiPoint1() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(1, 1), new Waypoint(0, 2), new Waypoint(7, 9) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(45),
        new DummyMoveCommand(Math.sqrt(2), 45), new DummyTurnCommand(360 - 45),
        new DummyMoveCommand(Math.sqrt(2), 360 - 45), new DummyTurnCommand(45),
        new DummyMoveCommand(Math.sqrt(98), 45));
    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void DiagonalPathATest() {
    Waypoint initialPoint = AtHomeChallengePoints.B2;

    Waypoint[] waypoints = { AtHomeChallengePoints.E6, AtHomeChallengePoints.A6, AtHomeChallengePoints.D11 };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(36.9),
        new DummyMoveCommand(150, 36.9), new DummyTurnCommand(270), new DummyMoveCommand(120, 270),
        new DummyTurnCommand(31.0), new DummyMoveCommand(175, 31.0));

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void DontMovePathTest() {
    Waypoint initialPoint = new Waypoint(-5, -8);

    Waypoint[] waypoints = { initialPoint };
    SequentialCommandGroup solution = new SequentialCommandGroup();

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);

  }

  @Test
  public void DontMovePathTest2() {
    Waypoint initialPoint = new Waypoint(-2, 4);

    Waypoint[] waypoints = { initialPoint, initialPoint, initialPoint, initialPoint };
    SequentialCommandGroup solution = new SequentialCommandGroup();

    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);

  }

  @Test
  public void multiPoint2() {
    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(1, 1), new Waypoint(1, 1), new Waypoint(1, 1), new Waypoint(0, 2),
        new Waypoint(0, 2), new Waypoint(0, 2), new Waypoint(7, 9), new Waypoint(7, 9) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(45),
        new DummyMoveCommand(Math.sqrt(2), 45), new DummyTurnCommand(360 - 45),
        new DummyMoveCommand(Math.sqrt(2), 360 - 45), new DummyTurnCommand(45),
        new DummyMoveCommand(Math.sqrt(98), 45));
    createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);

  }

}
