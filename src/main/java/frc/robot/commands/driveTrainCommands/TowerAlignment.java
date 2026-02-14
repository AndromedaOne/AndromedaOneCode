// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.driveTrainCommands;

import java.util.function.DoubleSupplier;

import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.drivetrain.DriveTrainBase;
import frc.robot.telemetries.Trace;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TowerAlignment extends SequentialCommandGroup4905 {
  /** Creates a new TowerAlignment. */
  // drivetrain is the drivetrain. obviously.
  // maxoutput is the maximum output allowed by the motor
  // distancesensorvalue should be passed in by the button maybe??? but it's
  // basically the value of tof1
  // we could stop passing it in and get it here... maybe????
  // angle is the rotation of tof1 in relation to the robot, only used for MUDS
  public TowerAlignment(DriveTrainBase drivetrain, double maxOutput,
      DoubleSupplier distanceSensorValue, DoubleSupplier angle) {

    // target distance for MUDS is a magic number! woaw
    // maybe put it in the config?
    // it is the distance we want to be from tof1 to the tower
    // target distance for MUDSD is 0, represents the desired distance between
    // tof2 and tof0 - maybe change the name?
    // the angle passed in is the angle the robot should move in I think?
    addCommands(new MoveUsingDistanceSensorDifference(drivetrain, 0.0, () -> 0, maxOutput),
        new MoveUsingDistanceSensor(drivetrain, distanceSensorValue, 5, angle, maxOutput));
  }

  @Override
  public void additionalEnd(boolean interrupted) {
    Trace.getInstance().logInfo("Tower Alignment ended");
  }
}
