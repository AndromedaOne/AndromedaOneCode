package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.pidcontroller.PIDController4905SampleStop;

public class TrackLineAndDriveBackwards extends TrackLineAndDrive {
    private static final double m_lineFollowingP = 0.25;
    private static final double m_lineFollowingI = 0.0;
    private static final double m_lineFollowingD = 0.035;

    public static final double BACK_DESIRED_COLOR_VALUE = 3.86;

    public TrackLineAndDriveBackwards() {
        super(new PIDController4905SampleStop(
            "BackColorSensorPID",
            m_lineFollowingP,
            m_lineFollowingI,
            m_lineFollowingD,
            0), 
            Robot.getInstance().getBackColorSensor(), 
            (output) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().move(-0.3, output, false),
            
            BACK_DESIRED_COLOR_VALUE);
    }


    @Override
    public boolean isFinished() {
        return false;
    }
    
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        Robot.getInstance().getSubsystemsContainer().getDrivetrain().stop();
    }
    
}
