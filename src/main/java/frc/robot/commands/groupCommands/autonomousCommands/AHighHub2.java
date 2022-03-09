// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Found issue. Command is lowercase, meanwhile in the code, Command is uppercase

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class AHighHub2 extends SequentialCommandGroup {
  /** Creates a new AhighHub2. */
  public AHighHub2() {
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    FeederBase feeder = subsystemsContainer.getFeeder();
    ShooterWheelBase topShooterWheel = subsystemsContainer.getTopShooterWheel();
    ShooterWheelBase bottomShooterWheel = subsystemsContainer.getBottomShooterWheel();
    ShooterAlignmentBase shooterAlignment = subsystemsContainer.getShooterAlignment();
    IntakeBase intake = subsystemsContainer.getIntake();
    final double shooterSetPoint = 1000;
    final double feederSetPoint = 1000;
    final double distanceToBall = 48.0;
    final double maxSpeed = 0.6;
    final boolean shootLow = false;
    MoveUsingEncoder moveCommand = new MoveUsingEncoder(driveTrain, distanceToBall, maxSpeed);

    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(moveCommand, new TurnToCompassHeading(13)
    );

    // new ParallelCommandGroup(moveCommand,
    // new PickUpCargo(feeder, topShooterWheel, bottomShooterWheel,
    // shooterAlignment, intake,
    // false)),
    // new ShootTarmac(feeder, topShooterWheel, bottomShooterWheel,
    // shooterAlignment, shootLow),
    // new StopShooterFeeder(feeder, topShooterWheel, bottomShooterWheel));
    // drive to the cargo and pick up with deploy and run intake command. We will
    // shoot the cargo with the location command
  }

}
