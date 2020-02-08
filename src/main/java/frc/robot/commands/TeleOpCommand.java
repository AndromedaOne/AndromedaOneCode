/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.oi.DriveController;
import frc.robot.oi.SubsystemController;
import frc.robot.subsystems.drivetrain.*;

/**
 * Allows you to drive the robot using the drive controller.
 */
public class TeleOpCommand extends CommandBase {

//Make the controllers a little easier to get to.  
  private DriveController m_driveController = Robot.getInstance().getOIContainer().getDriveController();
  private SubsystemController m_subsystemController = Robot.getInstance().getOIContainer().getSubsystemController();
  private DriveTrain m_driveTrain;

  /**
   * Takes inputs from the two joysticks on the drive controller.
   */
  public TeleOpCommand() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(Robot.getInstance().getSubsystemsContainer().getDrivetrain());
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_driveTrain = Robot.getInstance().getSubsystemsContainer().getDrivetrain();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double forwardBackwardStickValue = m_driveController.getForwardBackwardStick();
    double rotateStickValue = m_driveController.getRotateStick();

    m_driveTrain.moveUsingGyro(forwardBackwardStickValue, -rotateStickValue, true, true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
