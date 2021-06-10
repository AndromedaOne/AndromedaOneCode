package frc.robot.groupcommands.RomiCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.BringWingsUp;
import frc.robot.commands.LetWingsDown;
import frc.robot.commands.RunRomiShooter;
import frc.robot.commands.TimedCommand;
import frc.robot.commands.TrackLineAndDriveForward;
import frc.robot.commands.romiBallMopper.MopBallMopper;

public class RomiChallenge4 extends SequentialCommandGroup {

  public static final double TIME_TO_DRIVE_FORWARD = 10.4; // in seconds
  public static final double TIME_TO_LET_WINGS_DOWN = 4.5; // in seconds
  public static final double TIME_TO_BRING_WINGS_UP = 1.0; // in seconds
  public static final double TIME_BEFORE_FIRST_MOP = 3.3; // in seconds
  public static final double TIME_BETWEEN_FIRST_AND_SECOND_MOP = 6.6; // in seconds
  public static final double TIME_BETWEEN_SECOND_AND_THIRD_MOP = 9.9; // in seconds
  public static final double TIME_BETWEEN_FINAL_MOP_AND_LIFT_WING = 0.1;

  public RomiChallenge4() {

    addCommands(TimedCommand.create(new LetWingsDown(), TIME_TO_LET_WINGS_DOWN),
        new ParallelDeadlineGroup(TimedCommand.create(new TrackLineAndDriveForward(), TIME_TO_DRIVE_FORWARD),
            new RunRomiShooter()),
        new ParallelDeadlineGroup(new SequentialCommandGroup(TimedCommand.create(TIME_BETWEEN_FINAL_MOP_AND_LIFT_WING),
            TimedCommand.create(new BringWingsUp(), TIME_TO_BRING_WINGS_UP)), new MopBallMopper()));
  }

  private CommandBase getCommandsToDriveAlongPath() {
    SequentialCommandGroup moppingCommands = new SequentialCommandGroup(TimedCommand.create(TIME_BEFORE_FIRST_MOP),
        TimedCommand.create(TIME_BETWEEN_FIRST_AND_SECOND_MOP), TimedCommand.create(TIME_BETWEEN_SECOND_AND_THIRD_MOP));
    CommandBase drivingCommand = TimedCommand.create(new TrackLineAndDriveForward(), TIME_TO_DRIVE_FORWARD);
    return new ParallelCommandGroup(drivingCommand, moppingCommands);
  }

}
