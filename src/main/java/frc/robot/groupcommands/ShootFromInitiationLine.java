/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.drivetrain.DriveTrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ShootFromInitiationLine extends SequentialCommandGroup {
  /**
   * Creates a new ShootFromInitiationLine.
   */
  DriveTrain driveTrain = Robot.getInstance().getSubsystemsContainer().getDrivetrain();

  public ShootFromInitiationLine() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new DriveBackwardTimed(0.5, Robot.getInstance().getSubsystemsContainer().getDrivetrain()),
        new ShooterCommand(Robot.getInstance().getSubsystemsContainer().getShooter(), 1));
  }
}
