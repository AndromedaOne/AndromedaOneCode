// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class ShootTerminal extends SequentialCommandGroup {
  final double m_shooterSetpoint;
  final double m_shooterAngle;
  final double m_feederSetpoint;

  public ShootTerminal(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment) {
    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getShootLowHubButtonPressed()) {
      m_shooterSetpoint = 3000.0;
      m_shooterAngle = 42.7;
      m_feederSetpoint = 1.0;
    } else {
      m_shooterSetpoint = 3000.0;
      m_shooterAngle = 62.0;
      m_feederSetpoint = 1.0;
    }

    addCommands(new ShootCargo(feeder, topShooterWheel, bottomShooterWheel, shooterAlignment,
        () -> m_shooterSetpoint, () -> m_shooterAngle, () -> m_feederSetpoint));
  }
}
