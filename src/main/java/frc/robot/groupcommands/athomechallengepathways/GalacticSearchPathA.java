package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.intake.IntakeBase;

public class GalacticSearchPathA extends SequentialCommandGroup{
    private DriveTrain m_driveTrain;
    
    public GalacticSearchPathA(DriveTrain driveTrain, IntakeBase intake) {
        m_driveTrain = driveTrain;
        addCommands();
    }

    public SequentialCommandGroup getDriveTrainpathWay(){
        return new SequentialCommandGroup(
            new MoveUsingEncoder(m_driveTrain, 12),
            new TurnToCompassHeading(270)

        );
    }

}
