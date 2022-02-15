// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class DefaultShooterSystem extends SequentialCommandGroup {

  /** Creates a new DefaultShooter. */
  public DefaultShooterSystem(ShooterWheelBase topShooterWheel, ShooterWheelBase bottomShooterWheel,
      ShooterAlignmentBase shooterAlignment) {
    double defaultAngle = 0;
    addCommands(new InitializeShooterAlignment(shooterAlignment),
        new ParallelCommandGroup(
            new MoveShooterAlignment(shooterAlignment, () -> defaultAngle, false, 0, 0, 0)),
        new StopShooter(topShooterWheel, bottomShooterWheel));
  }
}
