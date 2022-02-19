// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.feeder.RealFeeder;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class ShootWallLow extends SequentialCommandGroup {
  private double m_shooterSetpoint;
  private double m_shooterAngle;
  private double m_feederSetpoint;

  public ShootWallLow(RealFeeder realFeeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment) {
    m_shooterSetpoint = 3000.0;
    m_shooterAngle = 47.0;
    m_feederSetpoint = 1000.0;

    addCommands(new ShootCargo(realFeeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
        () -> m_shooterSetpoint, () -> m_shooterAngle, () -> m_feederSetpoint));
  }
}