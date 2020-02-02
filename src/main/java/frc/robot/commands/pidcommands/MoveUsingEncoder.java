/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.pidcommands;

import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.pidcontroller.PIDController4905;
import frc.robot.subsystems.drivetrain.DriveTrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class MoveUsingEncoder extends PIDCommand {
  /**
   * Creates a new MoveUsingEncoder.
   */
  public MoveUsingEncoder(DriveTrain drivetrain, double distance) {
    super(
        // The controller that the command will use
        new PIDController4905(0, 0, 0, 0),
        // This should return the measurement
        drivetrain::getRobotPositionInches,
        // This should return the setpoint (can also be a constant)
        drivetrain.getRobotPositionInches() + distance,
        // This uses the output
        output -> {
          // Use the output here
          drivetrain.move(output, 0, false);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atSetpoint();
  }
}
