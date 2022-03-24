// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;

public class DefaultShooterAlignment extends SequentialCommandGroup {

  public DefaultShooterAlignment(ShooterAlignmentBase shooterAlignment) {
    final double defaultAngle = 1.5;
    addCommands(new InitializeShooterAlignment(shooterAlignment),
        new MoveShooterAlignment(shooterAlignment, () -> defaultAngle),
        new StopShooterAlignment(shooterAlignment));
  }
}
