package frc.robot.commands;

import java.lang.module.ModuleDescriptor.Requires;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.romiwings.RomiWingsBase;

public class LetWingsDown extends CommandBase{
    private Timer timer;
    private boolean usingTimer = false;
    private RomiWingsBase romiWings;
    private double m_time;

    public LetWingsDown(double time) {
        this();
        usingTimer = true;
        m_time = time;
        timer = new Timer();
    }

    public LetWingsDown() {
        romiWings = Robot.getInstance().getSubsystemsContainer().getWings();
        addRequirements(romiWings);
    }

    @Override
    public void initialize() {
        super.initialize();
        if(usingTimer) {
            timer.reset();
            timer.start();
        }
    }
    @Override
    public void execute() {
        super.execute();
        romiWings.letWingsDown();
    }

    @Override
    public boolean isFinished() {
        if(usingTimer) {
            return timer.hasElapsed(m_time);
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        romiWings.stop();
    }


    
}
