// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feederCommands.RunFeeder;
import frc.robot.commands.intakeCommands.DeployAndRunIntake;
import frc.robot.commands.shooterCommands.InitializeShooterAlignment;
import frc.robot.commands.shooterCommands.MoveShooterAlignment;
import frc.robot.commands.shooterCommands.RunShooterRPM;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class PickUpCargo extends SequentialCommandGroup {

  public PickUpCargo(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment,
      DoubleSupplier shooterSetpoint, DoubleSupplier feederSetpoint, IntakeBase intakeBase) {

    addCommands(new InitializeShooterAlignment(shooterAlignment),
        new MoveShooterAlignment(shooterAlignment, () -> 0), // angle for intake tbd
        new RunShooterRPM(topShooterWheel, bottomShooterWheel, -(shooterSetpoint.getAsDouble())),
        new RunFeeder(feeder, feederSetpoint.getAsDouble(), true, () -> false), // ready to
                                                                                // shoot will
                                                                                // not be used
                                                                                // when
        // picking up
        new DeployAndRunIntake(intakeBase, false));
  }
}