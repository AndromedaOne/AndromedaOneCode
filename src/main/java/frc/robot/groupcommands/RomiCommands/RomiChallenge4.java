package frc.robot.groupcommands.RomiCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.BringWingsUp;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.LetWingsDown;
import frc.robot.commands.TimedCommand;
import frc.robot.commands.TrackLineAndDriveForward;

public class RomiChallenge4 extends SequentialCommandGroup{

    public static final double TIME_TO_DRIVE_FORWARD = 10; // in seconds
    public static final double TIME_TO_LET_WINGS_DOWN = 4.0; // in seconds
    public static final double TIME_TO_BRING_WINGS_UP = 0.8; // in seconds

    public RomiChallenge4(){

        addCommands(
            TimedCommand.create(new LetWingsDown(), TIME_TO_LET_WINGS_DOWN),
            TimedCommand.create(new TrackLineAndDriveForward(), TIME_TO_DRIVE_FORWARD),
            TimedCommand.create(new BringWingsUp(), TIME_TO_BRING_WINGS_UP)
        );
    }
    
}
