// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Found issue. Command is lowercase, meanwhile in the code, Command is uppercase

package frc.robot.commands.groupCommands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.Timer;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.feederCommands.RunFeeder;
import frc.robot.commands.groupCommands.shooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.shooterFeederCommands.ShootCargo;
import frc.robot.commands.shooterCommands.MoveShooterAlignment;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class BHighHub2 extends SequentialCommandGroup {
  /** Creates a new AhighHub2. */
  public BHighHub2() {
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    FeederBase feeder = subsystemsContainer.getFeeder();
    ShooterWheelBase topShooterWheel = subsystemsContainer.getTopShooterWheel();
    ShooterWheelBase bottomShooterWheel = subsystemsContainer.getBottomShooterWheel();
    ShooterAlignmentBase shooterAlignment = subsystemsContainer.getShooterAlignment();
    IntakeBase intake = subsystemsContainer.getIntake();
    final double distanceToBall = 60.0;
    final double maxSpeed = 0.6;
    final double shooterSetpoint = 3300;
    final double shooterAngle = 64.5;
    final double feederSetpoint = 1.0;
    final long waitTime = 2500; // was 3500
    final long pauseWaitTime = 1000;
    MoveUsingEncoder moveCommand = new MoveUsingEncoder(driveTrain, distanceToBall, maxSpeed);
    addCommands(new SequentialCommandGroup(
        new ParallelDeadlineGroup(moveCommand,
            new PickUpCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment, intake,
                false)),
        new ParallelCommandGroup(new TurnToCompassHeading(144),
            new MoveShooterAlignment(shooterAlignment, () -> shooterAngle)),
        new ParallelDeadlineGroup(new Timer(40),
            new RunFeeder(feeder, () -> 0.5, true, () -> false)),
        new ParallelDeadlineGroup(new Timer(waitTime),
            new ShootCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
                () -> shooterSetpoint, () -> shooterAngle, () -> feederSetpoint),
            new PauseRobot(pauseWaitTime, driveTrain))));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);   
    Robot.getInstance().getSensorsContainer().getGyro().setInitialOffset(154.4);
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }

}