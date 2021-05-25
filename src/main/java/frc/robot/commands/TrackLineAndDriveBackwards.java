package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.pidcontroller.PIDController4905SampleStop;

public class TrackLineAndDriveBackwards extends TrackLineAndDrive {
    private static final double m_lineFollowingP = 0.75;
    private static final double m_lineFollowingI = 0.0;
    private static final double m_lineFollowingD = 0.075;

    public static final double BACK_DESIRED_COLOR_VALUE = 4.39;

    public TrackLineAndDriveBackwards() {
        super(new PIDController4905SampleStop(
            "BackColorSensorPID",
            m_lineFollowingP,
            m_lineFollowingI,
            m_lineFollowingD,
            0), 
            Robot.getInstance().getBackColorSensor(), 
            (output) -> Robot.getInstance().getSubsystemsContainer().getDrivetrain().move(-0.3, output * 0.3, false),
            // The output is multiplied by 0.3 in the move method because when the PID values were tuned the output 
            // was being multiplied by the forward backward stick value which was 0.3 at the time 
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
