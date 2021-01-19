package pathgeneratortests;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pathgeneration.pathgenerators.DPGAdjustingForRobotSides;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointWithRobotSide;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.utils.RobotDimensions;
import frc.robot.utils.RobotSections;
import mockedclasses.RobotDimensionsMocked;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class DPGAdjustingForRobotSidesTests {
  private List<Waypoint> m_waypoints = new ArrayList<Waypoint>();
  private RobotDimensions m_robotDimensions = RobotDimensionsMocked.getMockedRobotDimensions();

  private class DPGAdjustingForRobotSidesTester extends DPGAdjustingForRobotSides {

    List<CommandBase> m_commands;

    public DPGAdjustingForRobotSidesTester(WaypointsBase waypoints, Waypoint initialWaypoint, double initialHeading, RobotDimensions robotDimensions) {
      super(waypoints, initialWaypoint, initialHeading, robotDimensions);
      m_commands = new ArrayList<CommandBase>();
    }

    public DPGAdjustingForRobotSidesTester(WaypointsBase waypoints, Waypoint initialWaypoint, RobotDimensions robotDimensions) {
      super(waypoints, initialWaypoint, robotDimensions);
      m_commands = new ArrayList<CommandBase>();
    }

    @Override
    protected CommandBase createTurnCommand(double angle) {
      DummyTurnCommand dummyTurnCommand = new DummyTurnCommand(angle);
      m_commands.add(dummyTurnCommand);
      return dummyTurnCommand;
    }

    @Override
    protected CommandBase createMoveCommand(double distance, double angle) {
      DummyMoveCommand dummyMoveCommand = new DummyMoveCommand(distance, angle);
      m_commands.add(dummyMoveCommand);
      return dummyMoveCommand;
    }

    public List<CommandBase> getCommandsAdded() {
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

  private void createSimpleDPGWithRobotSideTest(Waypoint[] waypoints, Waypoint initialPoint, double initialHeading,
      List<CommandBase> solution) {
    for (Waypoint w : waypoints) {
      m_waypoints.add(w);
    }
    TestWaypoints testPoints = new TestWaypoints();
    DPGAdjustingForRobotSidesTester dPGAdjustingForRobotSidesTester;
    if(!Double.isNaN(initialHeading)){
        dPGAdjustingForRobotSidesTester = new DPGAdjustingForRobotSidesTester(testPoints, initialPoint, initialHeading, m_robotDimensions);
    }else {
        dPGAdjustingForRobotSidesTester = new DPGAdjustingForRobotSidesTester(testPoints, initialPoint, m_robotDimensions); 
    }
    dPGAdjustingForRobotSidesTester.getPath();

    DummyPathChecker.CompareDummyCommands(solution, dPGAdjustingForRobotSidesTester.getCommandsAdded());

  }

  private void createSimpleDPGWithRobotSideTest(Waypoint[] waypoints, Waypoint initialPoint,
      List<CommandBase> solution) {
        createSimpleDPGWithRobotSideTest(waypoints, initialPoint, Double.NaN, solution);
  }

  @BeforeEach
  public void clearWaypoints() {
    m_waypoints.clear();
  }

  @Test
  public void intialPointNoHeading() {
    WaypointWithRobotSide initialPoint = new WaypointWithRobotSide(0, 0, RobotSections.CENTER);
    
    TestWaypoints testPoints = new TestWaypoints();
    
    boolean threwException = false;
    try{
        new DPGAdjustingForRobotSidesTester(testPoints, initialPoint, m_robotDimensions);
    }catch(InvalidParameterException e) {
        threwException = true;
    }
    assert threwException;
  }

  @Test
  public void twoPointsOnEachOther() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 0, RobotSections.CENTER), new WaypointWithRobotSide(0, 0, RobotSections.CENTER) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    
    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.FRONTOFFRAME) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(20 - RobotDimensionsMocked.MOCKED_ROBOT_LENGTH/2, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned2() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.CENTER) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(20, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned3() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.BACKOFBUMPER) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(20 + RobotDimensionsMocked.MOCKED_ROBOT_LENGTH/2 + RobotDimensionsMocked.MOCKED_ROBOT_BUMPER_DEPTH, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned4() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 5, RobotSections.FRONTOFBUMPER) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH/2 + RobotDimensionsMocked.MOCKED_ROBOT_BUMPER_DEPTH), 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsVerticallyAlligned5() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 5, RobotSections.BACKOFBUMPER) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(180));
    solution.add(new DummyMoveCommand(1, 180));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAlligned() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(5, 0, RobotSections.FRONTOFFRAME) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH/2), 90));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void twoPointsHorizontallyAllignedNewCenter() {

    Waypoint initialPoint = new Waypoint(1, 1);
    Waypoint[] waypoints = { new WaypointWithRobotSide(6, 1, RobotSections.FRONTOFFRAME) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(90));
    solution.add(new DummyMoveCommand(5 - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH/2), 90));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void diagonalPoints() {

    Waypoint initialPoint = new Waypoint(0, 0);
    Waypoint[] waypoints = { new WaypointWithRobotSide(-1, -1, RobotSections.FRONTOFFRAME) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(225));
    solution.add(new DummyMoveCommand(Math.sqrt(2) - (RobotDimensionsMocked.MOCKED_ROBOT_LENGTH/2), 225));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, solution);
  }

  @Test
  public void pointsVerticallyAllignedWithRobotSectionCenter() {

    Waypoint initialPoint = new WaypointWithRobotSide(0, 0, RobotSections.BACKOFBUMPER);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 20, RobotSections.CENTER) };
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(0));
    solution.add(new DummyMoveCommand(14, 0));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, 0, solution);
  }

  @Test
  public void multiPoint1() {

    Waypoint initialPoint = new WaypointWithRobotSide(-1, 0, RobotSections.BACKOFBUMPER);
    Waypoint[] waypoints = { 
      new WaypointWithRobotSide(-1, 0, RobotSections.BACKOFBUMPER), 
      new WaypointWithRobotSide(19, 20, RobotSections.CENTER), 
      new WaypointWithRobotSide(18, 21, RobotSections.FRONTOFBUMPER)};
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(45));
    solution.add(new DummyMoveCommand(0, 45));
    solution.add(new DummyTurnCommand(45));
    solution.add(new DummyMoveCommand(Math.sqrt(800) - 6, 45));
    solution.add(new DummyTurnCommand(360 - 45));
    solution.add(new DummyMoveCommand(Math.sqrt(2) - 6, 360 - 45));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, 45, solution);
  }

  @Test
  public void multiPoint2() {

    Waypoint initialPoint = new WaypointWithRobotSide(-1, -1, RobotSections.CENTER);
    Waypoint[] waypoints = { new WaypointWithRobotSide(0, 0, RobotSections.BACKOFBUMPER)};
    ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
    solution.add(new DummyTurnCommand(225));
    solution.add(new DummyMoveCommand(m_robotDimensions.getLength()/2 + m_robotDimensions.getBumperThickness() - Math.sqrt(2), 225));

    createSimpleDPGWithRobotSideTest(waypoints, initialPoint, 0, solution);
  }

}
