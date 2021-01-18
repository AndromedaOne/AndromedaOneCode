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

    private List<Waypoint> m_waypoints = new ArrayList<Waypoint>();
    public static final double TOLERANCE = 0.01;

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

    private void createSimpleDiagonalPathGeneratorTest(Waypoint[] waypoints, Waypoint initialPoint, List<CommandBase> solution) {
        
        for(Waypoint w : waypoints){
            m_waypoints.add(w);
        }
        TestWaypoints testPoints = new TestWaypoints();

        DiagonalPathGeneratorTester diagonalPathGeneratorTester = new DiagonalPathGeneratorTester(testPoints, initialPoint);
       
        diagonalPathGeneratorTester.getPath();
        
        try{
            checkDummyCommandsAreEqual(solution, diagonalPathGeneratorTester.getCommandsAdded());
        }catch(AssertionError e) {

            printFailureInformation(solution, diagonalPathGeneratorTester.getCommandsAdded());
            throw(e);
        }
    }

    private void checkDummyCommandsAreEqual(List<CommandBase> commands1, List<CommandBase> commands2) {
        int index = 0;
        for(CommandBase solutionCommand : commands1) {
            CommandBase generatedCommand = commands2.get(index);
            if (solutionCommand instanceof DummyMoveCommand && generatedCommand instanceof DummyMoveCommand) {
                DummyMoveCommand c1 = (DummyMoveCommand) solutionCommand;
                DummyMoveCommand c2 = (DummyMoveCommand) generatedCommand;
                assert (Math.abs(c1.getAngle() - c2.getAngle()) <= TOLERANCE && Math.abs(c1.getDistance() - c2.getDistance()) <= TOLERANCE);

            }else if(solutionCommand instanceof DummyTurnCommand && generatedCommand instanceof DummyTurnCommand){
                DummyTurnCommand c1 = (DummyTurnCommand) solutionCommand;
                DummyTurnCommand c2 = (DummyTurnCommand) generatedCommand;
                assert (Math.abs(c1.getAngle() - c2.getAngle()) <= TOLERANCE);

            }else{
                assert false;
            }
            index++;
        }
        assert commands1.size() == commands2.size();
    }

    private void printFailureInformation(List<CommandBase> solution, List<CommandBase> generatedOutput){
        System.out.println("---------------------------------------");
        System.out.println("Expected: ");
        for(CommandBase c : solution) {
            System.out.println(c.toString());
        }
        System.out.println("Received: ");
        for(CommandBase c : generatedOutput) {
            System.out.println(c.toString());
        }
    }

    @BeforeEach
    public void clearWaypoints() {
        m_waypoints.clear();
    }

    @Test
    public void twoPointsVerticallyAlligned() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(0, 5)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(0));
        solution.add(new DummyMoveCommand(5, 0));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void twoPointsHorizontallyAlligned() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(5, 0)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(90));
        solution.add(new DummyMoveCommand(5, 90));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void twoPointsHorizontallyAllignedTurnLeft() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(-5, 0)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(270));
        solution.add(new DummyMoveCommand(5, 270));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void twoPointsVerticallyAllignedTurnAround() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(0, -4)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(180));
        solution.add(new DummyMoveCommand(4, 180));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void twoPointsDiagonalFromEachOther() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(2, 2)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(45));
        solution.add(new DummyMoveCommand(Math.sqrt(8), 45));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }
    @Test
    public void twoPointsDiagonalFromEachOther2() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(2, -2)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(135));
        solution.add(new DummyMoveCommand(Math.sqrt(8), 135));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void newInitialPosition1() {

        Waypoint initialPoint = new Waypoint(5,5);
        Waypoint[] waypoints = {
            new Waypoint(7, 7)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(45));
        solution.add(new DummyMoveCommand(Math.sqrt(8), 45));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void newInitialPosition2() {

        Waypoint initialPoint = new Waypoint(1,4);
        Waypoint[] waypoints = {
            new Waypoint(4, 8)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        double angleToTurnTo = Math.toDegrees(Math.atan(3.0/4.0));
        solution.add(new DummyTurnCommand(angleToTurnTo));
        solution.add(new DummyMoveCommand(5, angleToTurnTo));

        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }

    @Test
    public void multiPoint1() {

        Waypoint initialPoint = new Waypoint(0,0);
        Waypoint[] waypoints = {
            new Waypoint(1, 1),
            new Waypoint(0, 2),
            new Waypoint(7, 9)
        };
        ArrayList<CommandBase> solution = new ArrayList<CommandBase>();
        solution.add(new DummyTurnCommand(45));
        solution.add(new DummyMoveCommand(Math.sqrt(2), 45));
        solution.add(new DummyTurnCommand(360-45));
        solution.add(new DummyMoveCommand(Math.sqrt(2), 360-45));
        solution.add(new DummyTurnCommand(45));
        solution.add(new DummyMoveCommand(Math.sqrt(98), 45));
        createSimpleDiagonalPathGeneratorTest(waypoints, initialPoint, solution);
    }
}
