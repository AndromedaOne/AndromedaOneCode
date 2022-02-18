// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feederCommands.RunFeeder;
import frc.robot.commands.shooterCommands.InitializeShooterAlignment;
import frc.robot.commands.shooterCommands.MoveShooterAlignment;
import frc.robot.commands.shooterCommands.RunShooterRPM;
import frc.robot.subsystems.feeder.RealFeeder;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class RunShooterFeeder extends SequentialCommandGroup {

  public RunShooterFeeder(RealFeeder realFeeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment,
      DoubleSupplier setpoint, boolean runInReverse) {
    RunShooterRPM runShooterCommand = new RunShooterRPM(topShooterWheel, bottomShooterWheel,
        setpoint.getAsDouble());

    addCommands(new InitializeShooterAlignment(shooterAlignment),
        new MoveShooterAlignment(shooterAlignment, setpoint),
        new ParallelCommandGroup(runShooterCommand,
            new RunFeeder(realFeeder, 0, runInReverse, runShooterCommand.atSetpoint())));
  }
}
