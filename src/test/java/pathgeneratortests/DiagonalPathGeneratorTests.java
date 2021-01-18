package pathgeneratortests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.pathgeneration.pathgenerators.DiagonalPathGenerator;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;

public class DiagonalPathGeneratorTests {

    private class DiagonalPathGeneratorTester extends DiagonalPathGenerator {

        List<CommandBase> m_commands;

		public DiagonalPathGeneratorTester(WaypointsBase waypoints, Waypoint initialWaypoint) {
            super(waypoints, initialWaypoint);
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
        
        public List<CommandBase> getCommandsAdded(){
            return m_commands;
        }
        
    }

    private class TestWaypoints extends WaypointsBase {

        @Override
        protected void loadWaypoints() {
            for(Waypoint w : m_waypoints) {
                addWayPoint(w);
            }
        }

    }

    private List<Waypoint> m_waypoints = new ArrayList<>();
    
    @BeforeEach
    public void clearWaypoints() {
        m_waypoints.clear();

    }

    private void createSimpleDiagonalPathGeneratorTest(Waypoint[] waypoints, Waypoint initialPoint, CommandBase[] solution, boolean testMode) {
        
        for(Waypoint w : waypoints){
            m_waypoints.add(w);
        }
        TestWaypoints testPoints = new TestWaypoints();

        DiagonalPathGeneratorTester diagonalPathGeneratorTester = new DiagonalPathGeneratorTester(testPoints, initialPoint);

        List<CommandBase> correctCommands = new ArrayList<CommandBase>();
        for (CommandBase c : solution){
            correctCommands.add(c);
        }
        if(testMode) {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("Getting path.");
        }
        diagonalPathGeneratorTester.getPath();
        int index = 0;
        if(testMode) {
            for(CommandBase c : diagonalPathGeneratorTester.getCommandsAdded()) {
                System.out.println(c.toString());
            }
        }
        for(CommandBase c : solution) {
            assert c.equals(diagonalPathGeneratorTester.getCommandsAdded().get(index));
            index++;
        }
        assert solution.length == diagonalPathGeneratorTester.getCommandsAdded().size();
    }

    private void createSimpleDiagonalPathGeneratorTest(Waypoint[] waypoints, Waypoint initialPoint, CommandBase[] solution) {
        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution, false);
    }

    @Test
    public void twoPointsVerticallyAlligned() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(0, 4)
        };
        CommandBase[] solution = {
            new DummyTurnCommand(0),
            new DummyMoveCommand(4, 0)
        };

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    /*@Test
    public void twoPointsHorizontallyAlligned() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(5, 0)
        };
        CommandBase[] solution = {
            new DummyTurnCommand(90),
            new DummyMoveCommand(5, 90)
        };

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void twoPointsHorizontallyAllignedTurnLeft() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(-5, 0)
        };
        CommandBase[] solution = {
            new DummyTurnCommand(270),
            new DummyMoveCommand(5, 270)
        };

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }*/

    @Test
    public void twoPointsVerticallyAllignedTurnAround() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(0, -5)
        };
        CommandBase[] solution = {
            new DummyTurnCommand(180),
            new DummyMoveCommand(5, 180)
        };

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution, true);
    }
}
