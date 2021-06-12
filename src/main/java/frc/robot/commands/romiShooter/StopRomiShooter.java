package frc.robot.commands.romiShooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.romishooter.RomiShooterBase;
import frc.robot.telemetries.Trace;

public class StopRomiShooter extends CommandBase{
    RomiShooterBase romiShooter;

    public StopRomiShooter() {
        romiShooter = Robot.getInstance().getSubsystemsContainer().getRomiShooter();
        addRequirements(romiShooter);
    }

    @Override
    public void initialize() {
        super.initialize();
        Trace.getInstance().logCommandStart(this);
    }

    @Override
    public void execute() {
        super.execute();
        romiShooter.setSpeed(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        romiShooter.setSpeed(0.0);
        Trace.getInstance().logCommandStop(this);
    }
}
