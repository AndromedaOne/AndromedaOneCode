// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

public class TurnToTargetUsingGyro extends SequentialCommandGroup4905 {
  /** Creates a new TurnToTargetUsingGyro. */
  public TurnToTargetUsingGyro(DriveTrainBase driveTrainIntSupplier, IntSupplier wantedID,
      DoubleSupplier setpoint, boolean useSmartDashboard) {
    // PhotonVisionYawSupplier yawSupplier = new PhotonVisionYawSupplier(wantedID,
    // setpoint);
  }

  // Called when the command is initially scheduled.

}