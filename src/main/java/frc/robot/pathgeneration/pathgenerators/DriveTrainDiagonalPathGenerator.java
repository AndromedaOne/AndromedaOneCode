package frc.robot.pathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.pathgeneration.waypoints.Waypoint;
import frc.robot.pathgeneration.waypoints.WaypointsBase;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class DriveTrainDiagonalPathGenerator extends DiagonalPathGenerator {

    private DriveTrain m_driveTrain;

    public DriveTrainDiagonalPathGenerator(WaypointsBase waypoints, DriveTrain driveTrain) {
        super(waypoints);
        m_driveTrain = driveTrain;
    }

    /**
     * @param angle is a compassheading x such that 0<= x < 360 degrees
     */
    @Override
    protected CommandBase createTurnCommand(double angle) {
        return new TurnToCompassHeading(angle);
    }

    /**
     * @param angle is a compassheading x such that 0<= x < 360 degrees
     * 
     * @param distance is a distance in inches
     */
    @Override
    protected CommandBase createMoveCommand(double distance, double angle) {
        // TODO Auto-generated method stub
        return new MoveUsingEncoder(m_driveTrain, distance, angle);
    }
    
}
