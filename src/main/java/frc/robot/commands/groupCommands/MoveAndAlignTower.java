// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands;

import java.io.IOException;
import java.util.function.DoubleSupplier;

import org.json.simple.parser.ParseException;

import com.pathplanner.lib.util.FileVersionException;

import frc.robot.commands.driveTrainCommands.TowerAlignment;
import frc.robot.commands.driveTrainCommands.TurnToCompassHeading;
import frc.robot.commands.examplePathCommands.TowerPath;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;
import frc.robot.subsystems.drivetrain.DriveTrainBase;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MoveAndAlignTower extends SequentialCommandGroup4905 {
  /**
   * Creates a new MoveAndAlignTower.
   * 
   * @throws ParseException
   * @throws IOException
   * @throws FileVersionException
   */
  public MoveAndAlignTower(DriveTrainBase drivetrain, double maxOutput, DoubleSupplier angle,
      DistanceSensorBase tof) throws FileVersionException, IOException, ParseException {
    // Use addRequirements() here to declare subsystem dependencies.
    addCommands(new TowerPath(), new TurnToCompassHeading(() -> 0),
        new TowerAlignment(drivetrain, maxOutput, angle, tof));
  }

  // Returns true when the command should end.

}
