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
public class TurnToCompassHeading extends PIDCommand {
  double m_positonTolerance = 5;
  double m_velocityTolerance = 0.5;

  /**
   * Creates a new TurnToCompassHeading.
   * 
   * @param compassHeading input an angle in degrees.
   */
  public TurnToCompassHeading(double compassHeading) {
    super(
        // The controller that the command will use
        getPIDController(),
        // This should return the measurement
        NavXGyroSensor.getInstance()::getCompassHeading,
        // This should return the setpoint (can also be a constant)
        compassHeading,
        // This uses the output
        output -> {
          // Use the output here
          Robot.getInstance().getSubsystemsContainer().getDrivetrain().move(0, output, false);
        });
    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(m_positonTolerance, m_velocityTolerance);
    getController().enableContinuousInput(0, 360);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }

  private static PIDController getPIDController() {
    Config pidConfig = Config4905.getConfig4905().getPidConstantsConfig();
    double p = pidConfig.getDouble("GyroPIDCommands.TurningPTerm");
    double i = pidConfig.getDouble("GyroPIDCommands.TurningITerm");
    double d = pidConfig.getDouble("GyroPIDCommands.TurningDTerm");
    return new PIDController(p, i, d);
  }
}
