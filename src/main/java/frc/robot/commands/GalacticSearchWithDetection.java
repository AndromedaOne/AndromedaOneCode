/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Robot;
import frc.robot.commands.pidcommands.TurnToCompassHeading;

public class GalacticSearchWithDetection extends CommandBase {
  /**
   * Creates a new GalacticSearchWithDetection.
   */
  public GalacticSearchWithDetection() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    final double distanceToPowercell = Robot.getInstance().getSensorsContainer().getPowercellDetector()
        .getDistanceInches();
    if (distanceToPowercell <= 30) {
      CommandScheduler.getInstance().schedule(new TurnToCompassHeading(90));
    } else {
      CommandScheduler.getInstance().schedule(new TurnToCompassHeading(270));
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
