package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class DriveAndTurn extends CommandBase{
    DriveTrain m_driveTrain;
    int count = 0;
    private double m_turningSpeed;

    public DriveAndTurn(double turningSpeed) {
        m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDrivetrain();
        addRequirements(m_driveTrain);
        m_turningSpeed = turningSpeed;
    }

    @Override
    public void initialize() {
        super.initialize();
        count = 0;

    }

    @Override
    public void execute() {
        count++;
        double modifier = countModifier(count);
        m_driveTrain.move(-0.3, -m_turningSpeed - modifier, false);
    }

    private double countModifier(int count) {
        return count * 0.0;
    }

    @Override
    public void end(boolean interrupted) {
        m_driveTrain.stop();
    }
}
