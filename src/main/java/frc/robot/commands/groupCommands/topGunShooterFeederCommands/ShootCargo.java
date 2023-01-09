// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.topGunShooterFeederCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Timer;
import frc.robot.commands.topGunFeederCommands.RunFeeder;
import frc.robot.commands.topGunShooterCommands.InitializeShooterAlignment;
import frc.robot.commands.topGunShooterCommands.MoveShooterAlignment;
import frc.robot.commands.topGunShooterCommands.RunShooterRPM;
import frc.robot.subsystems.topGunFeeder.FeederBase;
import frc.robot.subsystems.topGunShooter.ShooterAlignmentBase;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

public class ShootCargo extends SequentialCommandGroup {

  public ShootCargo(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment,
      DoubleSupplier shooterSetpoint, DoubleSupplier angle, DoubleSupplier feederSetpoint) {

    RunShooterRPM runShooterCommand = new RunShooterRPM(topShooterWheel, bottomShooterWheel,
        shooterSetpoint);

    addCommands(new InitializeShooterAlignment(shooterAlignment),
        new ParallelDeadlineGroup(new MoveShooterAlignment(shooterAlignment, angle),
            new RunFeeder(feeder, feederSetpoint, true, () -> false)),
        new ParallelDeadlineGroup(new Timer(40),
            new RunFeeder(feeder, () -> 0.3, true, () -> false)),
        new ParallelCommandGroup(runShooterCommand,
            new RunFeeder(feeder, feederSetpoint, false, runShooterCommand.atSetpoint())));
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }

}
