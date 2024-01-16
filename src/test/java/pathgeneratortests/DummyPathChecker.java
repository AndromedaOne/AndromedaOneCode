package pathgeneratortests;

import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;

public class DummyPathChecker {
  public static final double TOLERANCE = 0.1;

  private static void checkDummyCommandsAreEqual(List<Command> commands1, List<Command> commands2) {
    int index = 0;
    for (Command solutionCommand : commands1) {
      Command generatedCommand = commands2.get(index);
      if (solutionCommand instanceof DummyMoveCommand
          && generatedCommand instanceof DummyMoveCommand) {
        DummyMoveCommand c1 = (DummyMoveCommand) solutionCommand;
        DummyMoveCommand c2 = (DummyMoveCommand) generatedCommand;
        assert (Math.abs(c1.getAngle() - c2.getAngle()) <= TOLERANCE
            && Math.abs(c1.getDistance() - c2.getDistance()) <= TOLERANCE);

      } else if (solutionCommand instanceof DummyTurnCommand
          && generatedCommand instanceof DummyTurnCommand) {
        DummyTurnCommand c1 = (DummyTurnCommand) solutionCommand;
        DummyTurnCommand c2 = (DummyTurnCommand) generatedCommand;
        assert (Math.abs(c1.getAngle() - c2.getAngle()) <= TOLERANCE);

      } else {
        assert false;
      }
      index++;
    }
    assert commands1.size() == commands2.size();
  }

  public static void CompareDummyCommands(List<Command> solution, List<Command> generatedOutput) {
    try {
      checkDummyCommandsAreEqual(solution, generatedOutput);
    } catch (AssertionError e) {

      printFailureInformation(solution, generatedOutput);
      throw (e);
    }
  }

  private static void printFailureInformation(List<Command> solution,
      List<Command> generatedOutput) {
    System.out.println("---------------------------------------");
    System.out.println("Expected: ");
    for (Command c : solution) {
      System.out.println(c.toString());
    }
    System.out.println("\nReceived: ");
    for (Command c : generatedOutput) {
      System.out.println(c.toString());
    }
  }
}
