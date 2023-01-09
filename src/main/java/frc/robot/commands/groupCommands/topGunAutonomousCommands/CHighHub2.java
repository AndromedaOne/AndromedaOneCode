// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// Found issue. Command is lowercase, meanwhile in the code, Command is uppercase

package frc.robot.commands.groupCommands.topGunAutonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.Timer;
import frc.robot.commands.driveTrainCommands.MoveUsingEncoder;
import frc.robot.commands.driveTrainCommands.PauseRobot;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.PickUpCargo;
import frc.robot.commands.groupCommands.topGunShooterFeederCommands.ShootCargo;
import frc.robot.commands.topGunFeederCommands.RunFeeder;
import frc.robot.commands.topGunShooterCommands.MoveShooterAlignment;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.topGunFeeder.FeederBase;
import frc.robot.subsystems.topGunIntake.IntakeBase;
import frc.robot.subsystems.topGunShooter.ShooterAlignmentBase;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CHighHub2 extends SequentialCommandGroup {
  /** Creates a new CHighHub2. */
  public CHighHub2() {
    SubsystemsContainer subsystemsContainer = Robot.getInstance().getSubsystemsContainer();
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    FeederBase feeder = subsystemsContainer.getFeeder();
    ShooterWheelBase topShooterWheel = subsystemsContainer.getTopShooterWheel();
    ShooterWheelBase bottomShooterWheel = subsystemsContainer.getBottomShooterWheel();
    ShooterAlignmentBase shooterAlignment = subsystemsContainer.getShooterAlignment();
    IntakeBase intake = subsystemsContainer.getIntake();
    final double distanceToBall = 60.0;
    final double maxSpeed = 1.0;
    final double shooterSetpoint = 3400; // was 3450
    final double shooterAngle = 64.5;
    final double feederSetpoint = 1.0;
    final long waitTime = 5000;

    MoveUsingEncoder moveCommand = new MoveUsingEncoder(driveTrain, distanceToBall, maxSpeed);

    addCommands(new SequentialCommandGroup(
        new ParallelDeadlineGroup(moveCommand,
            new PickUpCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment, intake,
                false)),
        // new ParallelCommandGroup(new TurnToCompassHeading(213),
        new MoveShooterAlignment(shooterAlignment, () -> shooterAngle),
        new ParallelDeadlineGroup(new Timer(40),
            new RunFeeder(feeder, () -> 0.5, true, () -> false)),
        new ParallelDeadlineGroup(new Timer(waitTime),
            new ShootCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
                () -> shooterSetpoint, () -> shooterAngle, () -> feederSetpoint),
            new PauseRobot(waitTime, driveTrain))));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    Robot.getInstance().getSensorsContainer().getGyro().setInitialOffset(213);
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }

}
