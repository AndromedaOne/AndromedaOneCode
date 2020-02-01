package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.subsystems.shooter.ShooterBase;

public class RunShooterWheelVelocity extends PIDCommand {
  private static PIDController m_pidController = new PIDController(0, 0, 0);
  private ShooterBase m_shooter;
  private double m_setpoint;

  public RunShooterWheelVelocity(ShooterBase shooter, double setpoint) {
    // PID Controller
    super(m_pidController,
        // Measurement
        shooter::getShooterWheelVelocity,
        // Setpoint
        setpoint,
        // Output
        output -> {
          shooter.setShooterWheelPower(output);
        },
        // Requirements
        Robot.getInstance().getSubsystemsContainer().getShooter());

    m_pidController.setTolerance(1);
      
    m_shooter = shooter;
    m_setpoint = setpoint;
  }

  @Override
  public void execute() {
    m_pidController.calculate(m_shooter.getShooterWheelVelocity(), m_setpoint);
    m_shooter.setPIDIsReady(getController().atSetpoint());
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}