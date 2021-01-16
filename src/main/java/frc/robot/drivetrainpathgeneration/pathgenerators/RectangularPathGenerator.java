package frc.robot.drivetrainpathgeneration.pathgenerators;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.drivetrainpathgeneration.waypoints.Waypoint;
import frc.robot.drivetrainpathgeneration.waypoints.Waypoints;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class RectangularPathGenerator extends PathGeneratorBase {

    public RectangularPathGenerator(DriveTrain drivetrain, Waypoints waypoints) {
        super(drivetrain, waypoints);
        // TODO Auto-generated constructor stub
    }

    @Override
    public CommandBase generatePath() {
        // TODO Auto-generated method stub
        SequentialCommandGroup path = new SequentialCommandGroup();
        double currentX = 0;
        double currentY = 0;

        while(super.hasNextWaypoint()) {
            Waypoint waypoint = super.getNextWaypointpoint();

            double deltaY = waypoint.getY() - currentY;
            double deltaX = waypoint.getX() - currentX;

            path.addCommands(
                new MoveUsingEncoder(getDriveTrain(), deltaY),
                new TurnToCompassHeading(90),
                new MoveUsingEncoder(getDriveTrain(), deltaX),
                new TurnToCompassHeading(0)
            );

            currentX += deltaX;
            currentY += deltaY;
        }

        return path;
    }

    
    
}
