package pathgeneratortests;

import java.security.InvalidParameterException;

import org.junit.Test;

import dummycommands.DummyMoveCommand;
import dummycommands.DummyPathChecker;
import dummycommands.DummyTurnCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.pathgenerators.DPGForWaypointsWithRobotSectionsToMove;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithRobotSectionToMove;
import frc.robot.pathgeneration.waypoints.Waypoints;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.RobotDimensions;
import frc.robot.utils.RobotSectionsForWaypointsToMove;
import mockedclasses.RobotDimensionsMocked;

public class DPGAdjustingForRobotSectionsToMoveTests {
  private static RobotDimensions m_robotDimensions = RobotDimensionsMocked.getMockedRobotDimensions();

  private class DPGForWaypointsWithRobotSectionsToMoveTester extends DPGForWaypointsWithRobotSectionsToMove {

    public DPGForWaypointsWithRobotSectionsToMoveTester(WaypointsBase waypoints, Waypoint initialWaypoint,
        double initialHeading, RobotDimensions robotDimensions) {
      super(waypoints, initialWaypoint, initialHeading, robotDimensions);
    }

    public DPGForWaypointsWithRobotSectionsToMoveTester(WaypointsBase waypoints, Waypoint initialWaypoint,
        RobotDimensions robotDimensions) {
      super(waypoints, initialWaypoint, robotDimensions);
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

  private void createTest(Waypoint[] waypoints, Waypoint initialPoint, double initialHeading,
      SequentialCommandGroup solution) {

    Waypoints testPoints = new Waypoints(waypoints);
    DPGForWaypointsWithRobotSectionsToMoveTester dPGForWaypointsWithRobotSectionsToMoveTester;

    if (!Double.isNaN(initialHeading)) {
      dPGForWaypointsWithRobotSectionsToMoveTester = new DPGForWaypointsWithRobotSectionsToMoveTester(testPoints,
          initialPoint, initialHeading, m_robotDimensions);
    } else {
      dPGForWaypointsWithRobotSectionsToMoveTester = new DPGForWaypointsWithRobotSectionsToMoveTester(testPoints,
          initialPoint, m_robotDimensions);
    }

    CommandBase generatedOutput = dPGForWaypointsWithRobotSectionsToMoveTester.getPath();

    DummyPathChecker.checkDummyCommandGroupsAreEquivalent(solution, generatedOutput);

  }

  private void createTest(Waypoint[] waypoints, Waypoint initialPoint, SequentialCommandGroup solution) {
    createTest(waypoints, initialPoint, Double.NaN, solution);
  }

  @Test
  public void intialPointNoHeading() {
    WaypointWithRobotSectionToMove initialPoint = new WaypointWithRobotSectionToMove(0, 0,
        RobotSectionsForWaypointsToMove.CENTER);

    Waypoints testPoints = new Waypoints();

    try {
      new DPGForWaypointsWithRobotSectionsToMoveTester(testPoints, initialPoint, m_robotDimensions);
      assert false;
    } catch (InvalidParameterException e) {

    }
  }

  @Test
  public void twoPointsOnEachOther() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 0, RobotSectionsForWaypointsToMove.CENTER),
        new WaypointWithRobotSectionToMove(0, 0, RobotSectionsForWaypointsToMove.CENTER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 20, RobotSectionsForWaypointsToMove.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(20 - RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2, 0));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned2() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 20, RobotSectionsForWaypointsToMove.CENTER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(20, 0));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned3() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 20, RobotSectionsForWaypointsToMove.BACKOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(180));
    solution.addCommands(new DummyMoveCommand(
        -(20 - RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2 - RobotDimensionsMocked.MOCKED_ROBOT_BUMPER_DEPTH), 180));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned4() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 5, RobotSectionsForWaypointsToMove.FRONTOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(
        5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2 + RobotDimensionsMocked.MOCKED_ROBOT_BUMPER_DEPTH), 0));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned5() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 5, RobotSectionsForWaypointsToMove.BACKOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(180));
    solution.addCommands(new DummyMoveCommand(1, 180));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2, 0,
        RobotSectionsForWaypointsToMove.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(90));
    solution.addCommands(new DummyMoveCommand(0, 90));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAllignedNewCenter() {

    Waypoint initialPoint = new Waypoint(1, 1);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(6, 1, RobotSectionsForWaypointsToMove.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(90));
    solution.addCommands(new DummyMoveCommand(5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2), 90));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void diagonalPoints() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(-1, -1, RobotSectionsForWaypointsToMove.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(225));
    solution.addCommands(new DummyMoveCommand(Math.sqrt(2) - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2), 225));

    createTest(waypoints, initialPoint, solution);
  }

  @Test
  public void pointsVerticallyAllignedWithRobotSectionCenter() {

    Waypoint initialPoint = new WaypointWithRobotSectionToMove(0, 0, RobotSectionsForWaypointsToMove.BACKOFBUMPER);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 20, RobotSectionsForWaypointsToMove.CENTER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(14, 0));

    createTest(waypoints, initialPoint, 0, solution);
  }

  @Test
  public void multiPoint1() {

    Waypoint initialPoint = new WaypointWithRobotSectionToMove(-1, 0, RobotSectionsForWaypointsToMove.BACKOFBUMPER);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(-1, 0, RobotSectionsForWaypointsToMove.BACKOFBUMPER),
        new WaypointWithRobotSectionToMove(19, 20, RobotSectionsForWaypointsToMove.CENTER),
        new WaypointWithRobotSectionToMove(18, 21, RobotSectionsForWaypointsToMove.FRONTOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(45));
    solution.addCommands(new DummyMoveCommand(0, 45));
    solution.addCommands(new DummyTurnCommand(45));
    solution.addCommands(new DummyMoveCommand(Math.sqrt(800) - 6, 45));
    solution.addCommands(new DummyTurnCommand(360 - 45));
    solution.addCommands(new DummyMoveCommand(Math.sqrt(2) - 6, 360 - 45));

    createTest(waypoints, initialPoint, 45, solution);
  }

  @Test
  public void multiPoint2() {

    Waypoint initialPoint = new WaypointWithRobotSectionToMove(-1, -1, RobotSectionsForWaypointsToMove.CENTER);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 0, RobotSectionsForWaypointsToMove.BACKOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(225));
    solution.addCommands(new DummyMoveCommand(
        m_robotDimensions.getLength() / 2 + m_robotDimensions.getBumperThickness() - Math.sqrt(2), 225));

    createTest(waypoints, initialPoint, 0, solution);
  }

  @Test
  public void multiPoint3() {

    Waypoint initialPoint = new WaypointWithRobotSectionToMove(1, 1, RobotSectionsForWaypointsToMove.CENTER);
    Waypoint[] waypoints = { 
      new WaypointWithRobotSectionToMove(1, 21, RobotSectionsForWaypointsToMove.CENTER),
      new WaypointWithRobotSectionToMove(21, 21, RobotSectionsForWaypointsToMove.CENTER),
      new WaypointWithRobotSectionToMove(1, 21, RobotSectionsForWaypointsToMove.CENTER)
    };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(20, 0));
    solution.addCommands(new DummyTurnCommand(90));
    solution.addCommands(new DummyMoveCommand(20, 90));
    solution.addCommands(new DummyTurnCommand(90));
    solution.addCommands(new DummyMoveCommand(-20, 90));

    createTest(waypoints, initialPoint, 0, solution);
  }

  @Test
  public void backOfRobotMove1() {

    Waypoint initialPoint = new WaypointWithRobotSectionToMove(0, 0, RobotSectionsForWaypointsToMove.CENTER);
    Waypoint[] waypoints = {
        new WaypointWithRobotSectionToMove(-20, -20, RobotSectionsForWaypointsToMove.BACKOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(45));
    solution.addCommands(new DummyMoveCommand(-(Math.sqrt(800) - m_robotDimensions.getLength() / 2), 45));

    createTest(waypoints, initialPoint, 0, solution);
  }

  @Test
  public void backOfRobotMove2() {

    Waypoint initialPoint = new WaypointWithRobotSectionToMove(4, 8, RobotSectionsForWaypointsToMove.FRONTOFBUMPER);
    Waypoint[] waypoints = {
        new WaypointWithRobotSectionToMove(4, -10.0, RobotSectionsForWaypointsToMove.BACKOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(-7, 0));

    createTest(waypoints, initialPoint, 0, solution);
  }

  @Test
  public void backOfRobotMove3() {

    Waypoint initialPoint = new WaypointWithRobotSectionToMove(0, 0, RobotSectionsForWaypointsToMove.CENTER);
    Waypoint[] waypoints = { new WaypointWithRobotSectionToMove(0, 20, RobotSectionsForWaypointsToMove.BACKOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(180));
    solution.addCommands(new DummyMoveCommand(-14, 180));

    createTest(waypoints, initialPoint, 0, solution);
  }

}
