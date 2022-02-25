// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class highHub2AutoFromWallToTerminal extends SequentialCommandGroup {
  /** Creates a new highHub2AutoFromWallToTerminal. */
  public highHub2AutoFromWallToTerminal() {
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    final double distanceToReverse = -10.0;
    final double maxSpeed = 0.6;
    final double compassHeading = -90.0;
    final double distanceToTerminal = 192.0;

    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new highHub2Auto(), new MoveUsingEncoder(driveTrain, distanceToReverse, maxSpeed),
        new TurnToCompassHeading(compassHeading),
        new MoveUsingEncoder(driveTrain, distanceToTerminal, maxSpeed));
  }
}
