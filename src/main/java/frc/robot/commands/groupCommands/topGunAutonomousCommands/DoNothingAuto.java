/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.groupCommands.topGunAutonomousCommands;

import edu.wpi.first.wpilibj2.command.Command;

public class DoNothingAuto extends Command {
  /**
   * Creates a new DoNothingAuto.
   */
  public DoNothingAuto() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
