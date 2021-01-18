package pathgeneratortests;

import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DummyPathChecker {
    public static final double TOLERANCE = 0.01;

    private static void checkDummyCommandsAreEqual(List<CommandBase> commands1, List<CommandBase> commands2) {
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

    public static void CompareDummyCommands(List<CommandBase> solution, List<CommandBase> generatedOutput) {
        try{
            checkDummyCommandsAreEqual(solution, generatedOutput);
        }catch(AssertionError e) {

            printFailureInformation(solution, generatedOutput);
            throw(e);
        }
    }

    private static void printFailureInformation(List<CommandBase> solution, List<CommandBase> generatedOutput){
        System.out.println("---------------------------------------");
        System.out.println("Expected: ");
        for(CommandBase c : solution) {
            System.out.println(c.toString());
        }
        System.out.println("\nReceived: ");
        for(CommandBase c : generatedOutput) {
            System.out.println(c.toString());
        }
    }
}
