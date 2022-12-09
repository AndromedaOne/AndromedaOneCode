// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.topGunShooterFeederCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.topGunFeederCommands.StopFeeder;
import frc.robot.commands.topGunShooterCommands.StopShooter;
import frc.robot.subsystems.topGunFeeder.FeederBase;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;

public class StopShooterFeeder extends SequentialCommandGroup {

  /** Creates a new StopShooterFeeder. */
  public StopShooterFeeder(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel) {
    addCommands(new StopFeeder(feeder), new StopShooter(topShooterWheel, bottomShooterWheel));
  }
}
