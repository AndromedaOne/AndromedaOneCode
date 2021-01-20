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
import frc.robot.pathgeneration.pathgenerators.RectangularPathGenerator;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class RectangularPathGeneratorTests {
  private List<Waypoint> m_waypoints = new ArrayList<Waypoint>();

  private class RectangularPathGeneratorTester extends RectangularPathGenerator {

    public RectangularPathGeneratorTester(WaypointsBase waypoints, Waypoint initialWaypoint) {
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

  private class TestWaypoints extends WaypointsBase {

    @Override
    protected void loadWaypoints() {
      for (Waypoint w : m_waypoints) {
        addWayPoint(w);
      }
    }

  }

  private void createSimpleRectangularPathGeneratorTest(Waypoint[] waypoints, Waypoint initialPoint,
      SequentialCommandGroup solution) {
    for (Waypoint w : waypoints) {
      m_waypoints.add(w);
    }
    TestWaypoints testPoints = new TestWaypoints();

    RectangularPathGeneratorTester rectangularPathGeneratorTester = new RectangularPathGeneratorTester(testPoints,
        initialPoint);

    CommandBase generatedOutput = rectangularPathGeneratorTester.getPath();

    DummyPathChecker.checkDummyCommandGroupsAreEquivalent(solution, generatedOutput);
  }

  @BeforeEach
  public void clearWaypoints() {
    m_waypoints.clear();
  }

  @Test
  public void twoPointsVerticallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(0, 5) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(0), new DummyMoveCommand(5, 0));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAllignedTurnLeft() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(-5, 0) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(270),
        new DummyMoveCommand(5, 270));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAllignedTurnAround() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(0, -4) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(180),
        new DummyMoveCommand(4, 180));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsDiagonalFromEachOther() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(2, 2) };
    SequentialCommandGroup solution = new SequentialCommandGroup();

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsDiagonalFromEachOther2() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(2, -2) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(180),
        new DummyMoveCommand(2, 180), new DummyTurnCommand(90), new DummyMoveCommand(2, 90));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void newInitialPosition1() {

    Waypoint initialPoint = new Waypoint(5, 5);
    Waypoint[] waypoints = { new Waypoint(7, 7) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(0), new DummyMoveCommand(2, 0),
        new DummyTurnCommand(90), new DummyMoveCommand(2, 90));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void newInitialPosition2() {

    Waypoint initialPoint = new Waypoint(1, 4);
    Waypoint[] waypoints = { new Waypoint(4, 8) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(0), new DummyMoveCommand(4, 0),
        new DummyTurnCommand(90), new DummyMoveCommand(3, 90));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void multiPoint1() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(1, 1), new Waypoint(0, 2), new Waypoint(7, 9) };
    SequentialCommandGroup solution = new SequentialCommandGroup(new DummyTurnCommand(0), new DummyMoveCommand(1, 0),
        new DummyTurnCommand(90), new DummyMoveCommand(1, 90), new DummyTurnCommand(0), new DummyMoveCommand(1, 0),
        new DummyTurnCommand(270), new DummyMoveCommand(1, 270), new DummyTurnCommand(0), new DummyMoveCommand(7, 0),
        new DummyTurnCommand(90), new DummyMoveCommand(7, 90));
    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }
}
