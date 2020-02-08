/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.pidcommands;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.sensors.NavXGyroSensor;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TurnDeltaAngle extends PIDCommand {
  private double m_deltaTurnAngle;
  private double m_targetAngle;
  private double m_positionTolerance = 5;
  private double m_velocityTolerance = 0.5;

  /**
   * Creates a new TurnDeltaAngle.
   */
  public TurnDeltaAngle(double deltaTurnAngle) {
    super(
        // The controller that the command will use
        getPidController(),
        // This should return the measurement
        NavXGyroSensor.getInstance()::getZAngle,
        // This should return the setpoint (can also be a constant)
        NavXGyroSensor.getInstance().getZAngle() + deltaTurnAngle,
        // This uses the output
        output -> {
          // Use the output here
          Robot.getInstance().getSubsystemsContainer().getDrivetrain().move(0, output, false);
        });
    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(m_positionTolerance, m_velocityTolerance);
    m_deltaTurnAngle = deltaTurnAngle;
  }

  @Override
  public void initialize() {
    super.initialize();
    double setpoint = NavXGyroSensor.getInstance().getZAngle() + m_deltaTurnAngle;
    double angle = NavXGyroSensor.getInstance().getZAngle();
    System.out.println(" - Starting Angle: " + angle + " - ");
    System.out.println(" - Setpoint: " + setpoint + " - ");
    m_setpoint = () -> setpoint;
    m_targetAngle = setpoint;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println(" - Finish Angled: " + NavXGyroSensor.getInstance().getZAngle() + " - ");
    return getController().atSetpoint();
  }

  private double getSetpoint() {
    return m_targetAngle;
  }

  private static PIDController getPidController() {
    Config pidConfig = Config4905.getConfig4905().getPidConstantsConfig();
    double p = pidConfig.getDouble("GyroPIDCommands.TurningPTerm");
    double i = pidConfig.getDouble("GyroPIDCommands.TurningITerm");
    double d = pidConfig.getDouble("GyroPIDCommands.TurningDTerm");
    return new PIDController(p, i, d);
  }
}
