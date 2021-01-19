/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class InterstellarAccuracyChallenge extends SequentialCommandGroup {
  private DriveTrain m_driveTrain;
  double maximumPower = 0.5;
  double robotLengthInches = 38;

  /**
   * Creates a new InterstellarAccuracyChallenge.
   */
  public InterstellarAccuracyChallenge(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder) {
    m_driveTrain = driveTrain;
    addCommands(new DelayedSequentialCommandGroup(
        // shoot from C3
        new ShootWithDistance(shooter, feeder, 0),
        // move to C8
        new MoveUsingEncoder(driveTrain, 0)));
    // move to C4

  }
}
