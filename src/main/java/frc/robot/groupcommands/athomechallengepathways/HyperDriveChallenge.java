/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;

public class HyperDriveChallenge extends SequentialCommandGroup {
  private DriveTrain m_driveTrain;
  double robotLengthInches = 38;
  double maximumPower = 0.5;

  /**
   * Creates a new HyperDriveChallenge.
   */
  public HyperDriveChallenge(DriveTrain driveTrain) {
    m_driveTrain = driveTrain;
    addCommands(
        new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, 30 + robotLengthInches / 2, 0, maximumPower),
            new TurnToCompassHeading(90), new MoveUsingEncoder(driveTrain, 90, 90, maximumPower),
            new TurnToCompassHeading(180), new MoveUsingEncoder(driveTrain, 60, 180, maximumPower),
            new TurnToCompassHeading(90), new MoveUsingEncoder(driveTrain, 90, 90, maximumPower),
            new TurnToCompassHeading(0), new MoveUsingEncoder(driveTrain, 60, 0, maximumPower),
            new TurnToCompassHeading(270), new MoveUsingEncoder(driveTrain, 60, 270, maximumPower),
            new TurnToCompassHeading(0), new MoveUsingEncoder(driveTrain, 60, 0, maximumPower),
            new TurnToCompassHeading(90), new MoveUsingEncoder(driveTrain, 30, 90, maximumPower),
            new TurnToCompassHeading(0), new MoveUsingEncoder(driveTrain, 60, 0, maximumPower),
            new TurnToCompassHeading(90), new MoveUsingEncoder(driveTrain, 180, 90, maximumPower),
            new TurnToCompassHeading(180), new MoveUsingEncoder(driveTrain, 60, 180, maximumPower),
            new TurnToCompassHeading(270), new MoveUsingEncoder(driveTrain, 90, 270, maximumPower),
            new TurnToCompassHeading(180), new MoveUsingEncoder(driveTrain, 60, 180, maximumPower)));
  }
}
