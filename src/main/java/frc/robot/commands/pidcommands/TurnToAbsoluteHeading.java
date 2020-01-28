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
public class TurnToAbsoluteHeading extends PIDCommand {
  double m_positonTolerance = 0.1;
  double m_velocityTolerance = 0.5;

  /**
   * Creates a new TurnToAbsoluteHeading.
   */
  public TurnToAbsoluteHeading(double compassHeading) {
    super(
        // The controller that the command will use
        getPIDController(),
        // This should return the measurement
        NavXGyroSensor.getInstance()::getCompassHeading,
        // This should return the setpoint (can also be a constant)
        getModAngle(compassHeading),
        // This uses the output
        output -> {
          // Use the output here
          Robot.getInstance().getSubsystemsContainer().getDrivetrain().move(0, output, false);
        });
    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(m_positonTolerance, m_velocityTolerance);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }

  private static PIDController getPIDController() {
    Config pidConfig = Config4905.getConfig4905().getPidConstantsConfig();
    double p = pidConfig.getDouble("TurningPTerm");
    double i = pidConfig.getDouble("TurningITerm");
    double d = pidConfig.getDouble("TurningDTerm");
    return new PIDController(p, i, d);
  }

  private static double getModAngle(double heading) {
    double deltaAngle = heading - NavXGyroSensor.getInstance().getCompassHeading();
    System.out.println("Raw Delta Angle: " + deltaAngle);
    // This corrects turn that are over 180
    if (deltaAngle > 180) {
      deltaAngle = -(360 - deltaAngle);
      System.out.println("Angle corrected for shortest method, New Delta: " + deltaAngle);
    } else if (deltaAngle < -180) {
      deltaAngle = 360 + deltaAngle;
      System.out.println("Angle corrected for shortest method, New Delta: " + deltaAngle);
    }
    return deltaAngle;
  }
}
