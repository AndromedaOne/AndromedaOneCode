package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.subsystems.shooter.ShooterBase;
import edu.wpi.first.wpilibj.controller.PIDController;

public class RunShooterSeriesVelocity extends PIDCommand {

    private static PIDController m_pidController = new PIDController(0, 0, 0);

    public RunShooterSeriesVelocity(ShooterBase shooter, double setpoint) {
        // PID Controller
        super(m_pidController,
            // Measurement
            shooter::getShooterSeriesVelocity,
            // Setpoint
            setpoint,
            // Output
            output -> {
              shooter.setShooterSeriesPower(output);
            },
            // Requirements
            Robot.getInstance().getSubsystemsContainer().getShooter());

        m_pidController.setTolerance(1);
      }



}