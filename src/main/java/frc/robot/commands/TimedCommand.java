package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

public class TimedCommand extends CommandBase{
    private Timer timer;
    private double m_time;
    
    public static CommandBase create(CommandBase command, double time){
        ParallelDeadlineGroup parallelDeadlineGroup = new ParallelDeadlineGroup(new TimedCommand(time), command);
        return parallelDeadlineGroup;

    } 
    private TimedCommand(double time) {
        timer = new Timer();
        m_time = time;
    }

    @Override
    public void initialize() {
        super.initialize();
        timer.reset();
        timer.start();
    }
    @Override
    public boolean isFinished() {
        return timer.hasElapsed(m_time);
    }
}
