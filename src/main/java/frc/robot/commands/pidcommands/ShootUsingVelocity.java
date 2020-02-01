package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.subsystems.shooter.ShooterBase;

public class ShootUsingVelocity extends PIDCommand {
  private static PIDController m_pidController = new PIDController(0, 0, 0);
  private ShooterBase m_shooter;
  private double m_setpoint;

  public ShootUsingVelocity(ShooterBase shooter, double setpoint) {
    // PID Controller
    super(m_pidController,
        // Measurement
        shooter::getShooterVelocity,
        // Setpoint
        setpoint,
        // Output
        output -> {
          shooter.setShooterPower(output);
        },
        // Requirements
        Robot.getInstance().getSubsystemsContainer().getShooter());

    m_shooter = shooter;
    m_setpoint = setpoint;
  }

  @Override
  public void execute() {
    m_pidController.calculate(m_shooter.getShooterVelocity(), m_setpoint);
    m_shooter.setPIDIsReady(getController().atSetpoint());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}