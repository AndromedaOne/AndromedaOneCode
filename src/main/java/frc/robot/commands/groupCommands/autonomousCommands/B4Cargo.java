// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.Timer;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.groupCommands.shooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootTerminal;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class B4Cargo extends SequentialCommandGroup {
  /** Creates a new B4Cargo. */
  public B4Cargo() {
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    FeederBase feeder = subsystemsContainer.getFeeder();
    ShooterWheelBase topShooterWheel = subsystemsContainer.getTopShooterWheel();
    ShooterWheelBase bottomShooterWheel = subsystemsContainer.getBottomShooterWheel();
    ShooterAlignmentBase shooterAlignment = subsystemsContainer.getShooterAlignment();
    IntakeBase intakeBase = subsystemsContainer.getIntake();
    long waitTime = 4000;

    addCommands(new BHighHub2(), new TurnToCompassHeading(180),
        new MoveUsingEncoder(driveTrain, 133, 1), new TurnToCompassHeading(135),
        new ParallelDeadlineGroup(new MoveUsingEncoder(driveTrain, 53, 1),
            new PickUpCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
                intakeBase, false)),
        new ParallelDeadlineGroup(new Timer(waitTime),
            new PickUpCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
                intakeBase, false),
            new PauseRobot(waitTime, driveTrain)),
        new TurnToCompassHeading(153),
        new ParallelDeadlineGroup(new Timer(waitTime),
            new ShootTerminal(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment, false),
            new PauseRobot(waitTime, driveTrain)));

  }
}
