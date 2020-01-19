/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.DriveTrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DriveForward extends PIDCommand {
  /**
   * Creates a new DriveForward.
   */
  private DriveTrain m_driveTrain;
  public DriveForward(DriveTrain driveTrain) {
    super(
        // The controller that the command will use
        new PIDController(0.003, 0, 0),
        // This should return the measurement
         driveTrain::getPosition,
        // This should return the setpoint (can also be a constant)
        () -> 500,
        // This uses the output
        output -> {
          driveTrain.move(-output, 0);
        });
        m_driveTrain = driveTrain;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveTrain);
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(10);
    driveTrain.resetEncoder();
    System.out.println("DriveForward");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println("isFinished ticks = " 
    + m_driveTrain.getPosition());
    return (getController().atSetpoint());
  }

    public void initialize() {
      System.out.println("DriveForward initialize");
      m_driveTrain.resetEncoder();
      getController().reset();
    }
}
