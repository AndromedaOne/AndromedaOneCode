/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Robot;
import frc.robot.sensors.NavXGyroSensor;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class TurnDeltaAngle extends PIDCommand {
  double m_positionTolerance = 0.1;
  double m_velocityTolerance = 0.5;

  /**
   * Creates a new TurnDeltaAngle.
   */
  public TurnDeltaAngle(double deltaTurnAngle) {
    super(
        // The controller that the command will use
        new PIDController(0, 0, 0),
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
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
