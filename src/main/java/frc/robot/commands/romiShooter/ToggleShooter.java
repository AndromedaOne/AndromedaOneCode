package frc.robot.commands.romiShooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.subsystems.romishooter.RomiShooterBase;
import frc.robot.telemetries.Trace;

public class ToggleShooter extends CommandBase{
    private RomiShooterBase m_romiShooter;

    public ToggleShooter() {
        m_romiShooter = Robot.getInstance().getSubsystemsContainer().getRomiShooter();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        Trace.getInstance().logCommandStart(this);
        if (m_romiShooter.isShooterRunning()) {
            CommandScheduler.getInstance().schedule(new StopRomiShooter()); 
        } else {
            CommandScheduler.getInstance().schedule(new RunRomiShooter());
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        Trace.getInstance().logCommandStop(this);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return true;
    }
}
