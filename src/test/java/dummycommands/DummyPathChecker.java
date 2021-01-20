package dummycommands;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DummyPathChecker {
  public static final double TOLERANCE = 0.1;

  private static List<CommandBase> commandsScheduled = new ArrayList<CommandBase>();

  public static void registerCommandAsRunning(CommandBase command) {
    commandsScheduled.add(command);
  }

  public static void checkDummyCommandGroupsAreEquivalent(CommandBase solution, CommandBase generatedOutput) {
    checkCommandIsSequentialGroupCommand(solution);
    checkCommandIsSequentialGroupCommand(generatedOutput);

    List<DummyCommandBase> solutionCommands = runAndRecordSequentialCommandGroup(solution);
    List<DummyCommandBase> generatedCommands = runAndRecordSequentialCommandGroup(generatedOutput);
    CompareDummyCommands(solutionCommands, generatedCommands);

  }

  private static void checkCommandIsSequentialGroupCommand(CommandBase c) {
    try {
      assert (c instanceof SequentialCommandGroup);
    } catch (AssertionError e) {
      System.out.println("The command " + c.getName() + " is not a command group!");
      throw (e);
    }
  }

  private static List<DummyCommandBase> runAndRecordSequentialCommandGroup(CommandBase c) {
    List<DummyCommandBase> commands;
    runSequentialCommandGroup(c);
    try {
      commands = recordSequentialCommandGroup();
      return commands;
    } catch (AssertionError e) {
      System.out.println("Command group " + c.getName() + " has a command in it that is not a Dummy Command!");
      throw e;
    }
  }

  private static List<DummyCommandBase> recordSequentialCommandGroup() {
    List<DummyCommandBase> commands = new ArrayList<DummyCommandBase>();
    try {
      for (CommandBase c : commandsScheduled) {
        commands.add((DummyCommandBase) c);
      }

    } catch (ClassCastException e) {
      assert false;
    }
    return commands;
  }

  private static void runSequentialCommandGroup(CommandBase c) {
    commandsScheduled.clear();
    int i = 0;

    c.initialize();
    c.execute();

    while (!c.isFinished() && i <= 1000) {
      i++;
      c.execute();
    }

    // this command group is interrupted if it is not finished after 1000 cycles
    c.end(i > 1000);
    try {
      assert i <= 1000;
    } catch (AssertionError e) {
      System.out.println("Tried running command group " + c.getName() + " 1000 times and did not finish");
      throw e;
    }
  }

  private static void checkDummyCommandsAreEqual(List<DummyCommandBase> commands1, List<DummyCommandBase> commands2) {
    assert commands1.size() == commands2.size();
    int index = 0;
    for (DummyCommandBase c1 : commands1) {
      DummyCommandBase c2 = commands2.get(index);
      assert c1.isEquivalentTo(c2);
      index++;
    }

  }

  private static void CompareDummyCommands(List<DummyCommandBase> solution, List<DummyCommandBase> generatedOutput) {
    try {
      checkDummyCommandsAreEqual(solution, generatedOutput);
    } catch (AssertionError e) {
      printFailureInformation(solution, generatedOutput);
      throw (e);
    }
  }

  private static void printFailureInformation(List<DummyCommandBase> solution, List<DummyCommandBase> generatedOutput) {
    System.out.println("---------------------------------------");
    System.out.println("Expected: ");
    for (CommandBase c : solution) {
      System.out.println(c.toString());
    }
    System.out.println("\nReceived: ");
    for (CommandBase c : generatedOutput) {
      System.out.println(c.toString());
    }
  }
}
