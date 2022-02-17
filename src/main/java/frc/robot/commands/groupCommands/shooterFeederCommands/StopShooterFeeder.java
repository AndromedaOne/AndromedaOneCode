// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feederCommands.StopFeeder;
import frc.robot.commands.shooterCommands.StopShooter;
import frc.robot.subsystems.feeder.RealFeeder;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class StopShooterFeeder extends SequentialCommandGroup {

  /** Creates a new StopShooterFeeder. */
  public StopShooterFeeder(RealFeeder realFeeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel) {
    addCommands(new StopFeeder(realFeeder), new StopShooter(topShooterWheel, bottomShooterWheel));
  }
}
