/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.climber.ClimberBase;

public class ManualClimb extends CommandBase {
  ClimberBase climber = Robot.getInstance().getSubsystemsContainer().getClimber();
  XboxController controller;

  /**
   * Creates a new ManualClimb.
   */
  public ManualClimb() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(climber);
    controller = new XboxController(0);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // If the right trigger is pressed down
    if (controller.getTriggerAxis(Hand.kRight) >= 0) {
      climber.adjustRightWinch(controller.getTriggerAxis(Hand.kRight));
    } else if (controller.getTriggerAxis(Hand.kLeft) <= 0) {
      // If the left trigger is pressed
      climber.adjustLeftWinch(controller.getTriggerAxis(Hand.kLeft));
    } else {
      // if neither is pressed
      climber.stopWinch();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.stopWinch();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
