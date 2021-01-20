package pathgeneratortests;

import java.security.InvalidParameterException;

import org.junit.Test;

import dummycommands.DummyMoveCommand;
import dummycommands.DummyPathChecker;
import dummycommands.DummyTurnCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.pathgeneration.pathgenerators.DPGAdjustingForRobotSections;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithRobotSide;
import frc.robot.pathgeneration.waypoints.Waypoints;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.RobotDimensions;
import frc.robot.utils.RobotSections;
import mockedclasses.RobotDimensionsMocked;

public class DPGAdjustingForRobotSidesTests {
  private static RobotDimensions m_robotDimensions = RobotDimensionsMocked.getMockedRobotDimensions();

  private class DPGAdjustingForRobotSectionsTester extends DPGAdjustingForRobotSections {

    public DPGAdjustingForRobotSectionsTester(WaypointsBase waypoints, Waypoint initialWaypoint, double initialHeading,
        RobotDimensions robotDimensions) {
      super(waypoints, initialWaypoint, initialHeading, robotDimensions);
    }

    public DPGAdjustingForRobotSectionsTester(WaypointsBase waypoints, Waypoint initialWaypoint,
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

  private void createSimpleDPGWithRobotSideTest(Waypoint[] waypoints, Waypoint initialPoint, double initialHeading,
      SequentialCommandGroup solution) {

    Waypoints testPoints = new Waypoints(waypoints);
    DPGAdjustingForRobotSectionsTester dPGAdjustingForRobotSectionsTester;

    if (!Double.isNaN(initialHeading)) {
      dPGAdjustingForRobotSectionsTester = new DPGAdjustingForRobotSectionsTester(testPoints, initialPoint,
          initialHeading, m_robotDimensions);
    } else {
      dPGAdjustingForRobotSectionsTester = new DPGAdjustingForRobotSectionsTester(testPoints, initialPoint,
          m_robotDimensions);
    }

    CommandBase generatedOutput = dPGAdjustingForRobotSectionsTester.getPath();

    DummyPathChecker.checkDummyCommandGroupsAreEquivalent(solution, generatedOutput);

  }

  private void createSimpleDPGWithRobotSideTest(Waypoint[] waypoints, Waypoint initialPoint,
      SequentialCommandGroup solution) {
    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, Double.NaN, solution);
  }

  @Test
  public void intialPointNoHeading() {
    WaypointWithRobotSide initialPoint = new WaypointWithRobotSide(0, 0, RobotSections.CENTER);

    Waypoints testPoints = new Waypoints();

    boolean threwException = false;
    try {
      new DPGAdjustingForRobotSectionsTester(testPoints, initialPoint, m_robotDimensions);
    } catch (InvalidParameterException e) {
      threwException = true;
    }
    assert threwException;
  }

  @Test
  public void twoPointsOnEachOther() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 0, RobotSections.CENTER),
        new WaypointWithRobotSide(0, 0, RobotSections.CENTER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(20 - RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned2() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.CENTER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(20, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned3() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.BACKOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(
        20 + RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2 + RobotDimensionsMocked.MOCKED_ROBOT_BUMPER_DEPTH, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned4() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 5, RobotSections.FRONTOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(
        5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2 + RobotDimensionsMocked.MOCKED_ROBOT_BUMPER_DEPTH), 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned5() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 5, RobotSections.BACKOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(180));
    solution.addCommands(new DummyMoveCommand(1, 180));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(5, 0, RobotSections.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(90));
    solution.addCommands(new DummyMoveCommand(5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2), 90));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAllignedNewCenter() {

    Waypoint initialPoint = new Waypoint(1, 1);
    Waypoint[] waypoints = { new WaypointWithRobotSide(6, 1, RobotSections.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(90));
    solution.addCommands(new DummyMoveCommand(5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2), 90));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void diagonalPoints() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(-1, -1, RobotSections.FRONTOFFRAME) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(225));
    solution.addCommands(new DummyMoveCommand(Math.sqrt(2) - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH / 2), 225));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void pointsVerticallyAllignedWithRobotSectionCenter() {

    Waypoint initialPoint = new WaypointWithRobotSide(0, 0, RobotSections.BACKOFBUMPER);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.CENTER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(0));
    solution.addCommands(new DummyMoveCommand(14, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, 0, solution);
  }

  @Test
  public void multiPoint1() {

    Waypoint initialPoint = new WaypointWithRobotSide(-1, 0, RobotSections.BACKOFBUMPER);
    Waypoint[] waypoints = { new WaypointWithRobotSide(-1, 0, RobotSections.BACKOFBUMPER),
        new WaypointWithRobotSide(19, 20, RobotSections.CENTER),
        new WaypointWithRobotSide(18, 21, RobotSections.FRONTOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(45));
    solution.addCommands(new DummyMoveCommand(0, 45));
    solution.addCommands(new DummyTurnCommand(45));
    solution.addCommands(new DummyMoveCommand(Math.sqrt(800) - 6, 45));
    solution.addCommands(new DummyTurnCommand(360 - 45));
    solution.addCommands(new DummyMoveCommand(Math.sqrt(2) - 6, 360 - 45));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, 45, solution);
  }

  @Test
  public void multiPoint2() {

    Waypoint initialPoint = new WaypointWithRobotSide(-1, -1, RobotSections.CENTER);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 0, RobotSections.BACKOFBUMPER) };
    SequentialCommandGroup solution = new SequentialCommandGroup();
    solution.addCommands(new DummyTurnCommand(225));
    solution.addCommands(new DummyMoveCommand(
        m_robotDimensions.getLength() / 2 + m_robotDimensions.getBumperThickness() - Math.sqrt(2), 225));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, 0, solution);
  }

}
