package pathgeneratortests;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.pathgeneration.pathgenerators.RectangularPathGenerator;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class RectangularPathGeneratorTests {
  private List<Waypoint> m_waypoints = new ArrayList<Waypoint>();

  private class RectangularPathGeneratorTester extends RectangularPathGenerator {

    List<Command> m_commands;

    public RectangularPathGeneratorTester(String pathName, WaypointsBase waypoints,
        Waypoint initialWaypoint) {
      super(pathName, waypoints, initialWaypoint);
      m_commands = new ArrayList<Command>();
    }

    @Override
    protected Command createTurnCommand(double angle) {
      DummyTurnCommand dummyTurnCommand = new DummyTurnCommand(angle);
      m_commands.add(dummyTurnCommand);
      return dummyTurnCommand;
    }

    @Override
    protected Command createMoveCommand(double distance, double angle) {
      DummyMoveCommand dummyMoveCommand = new DummyMoveCommand(distance, angle);
      m_commands.add(dummyMoveCommand);
      return dummyMoveCommand;
    }

    public List<Command> getCommandsAdded() {
      return m_commands;
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
      List<Command> solution) {
    for (Waypoint w : waypoints) {
      m_waypoints.add(w);
    }
    TestWaypoints testPoints = new TestWaypoints();

    RectangularPathGeneratorTester rectangularPathGeneratorTester = new RectangularPathGeneratorTester(
        getClass().getSimpleName(), testPoints, initialPoint);

    rectangularPathGeneratorTester.getPath();

    DummyPathChecker.CompareDummyCommands(solution,
        rectangularPathGeneratorTester.getCommandsAdded());
  }

  @BeforeEach
  public void clearWaypoints() {
    m_waypoints.clear();
  }

  @Test
  public void twoPointsVerticallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(0, 5) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(5, 0));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAllignedTurnLeft() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(-5, 0) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(270));
    solution.add(new DummyMoveCommand(5, 270));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAllignedTurnAround() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(0, -4) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(180));
    solution.add(new DummyMoveCommand(4, 180));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsDiagonalFromEachOther() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(2, 2) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(2, 0));
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(2, 90));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsDiagonalFromEachOther2() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(2, -2) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(180));
    solution.add(new DummyMoveCommand(2, 180));
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(2, 90));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void newInitialPosition1() {

    Waypoint initialPoint = new Waypoint(5, 5);
    Waypoint[] waypoints = { new Waypoint(7, 7) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(2, 0));
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(2, 90));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void newInitialPosition2() {

    Waypoint initialPoint = new Waypoint(1, 4);
    Waypoint[] waypoints = { new Waypoint(4, 8) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(4, 0));
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(3, 90));

    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }

  @Test
  public void multiPoint1() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new Waypoint(1, 1), new Waypoint(0, 2), new Waypoint(7, 9) };
    ArrayList<Command> solution = new ArrayList<Command>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(1, 0));
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(1, 90));
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(1, 0));
    solution.add(new DummyTurnCommand(270));
    solution.add(new DummyMoveCommand(1, 270));
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(7, 0));
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(7, 90));
    createSimpleRectangularPathGeneratorTest(waypoints, initialPoint, solution);
  }
}
