// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

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
      IntakeBase intakeBase, boolean reverse) {

    final double m_shooterSetpoint = -1000.0;
    final double m_feederSetpoint = 0.5;

    addCommands(new InitializeShooterAlignment(shooterAlignment),
        new MoveShooterAlignment(shooterAlignment, () -> 0), // angle for intake tbd
        new RunShooterRPM(topShooterWheel, bottomShooterWheel, m_shooterSetpoint),
        new RunFeeder(feeder, m_feederSetpoint, true, () -> false),
        new DeployAndRunIntake(intakeBase, reverse));
  }
}