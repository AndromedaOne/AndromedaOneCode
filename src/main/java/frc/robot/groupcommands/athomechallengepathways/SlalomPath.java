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

public class SlalomPath extends SequentialCommandGroup {
  private DriveTrain m_driveTrain;
  double robotLengthInches = 40;
  double maximumPower = 0.5;

  /**
   * Creates a new SlalomPath.
   */
  public SlalomPath(DriveTrain driveTrain) {
    m_driveTrain = driveTrain;
    addCommands(new DelayedSequentialCommandGroup(
        new MoveUsingEncoder(m_driveTrain, 30 + robotLengthInches / 2, 0, maximumPower), // step 1
        new TurnToCompassHeading(270), new MoveUsingEncoder(m_driveTrain, 60, 270, maximumPower), // step 2
        new TurnToCompassHeading(0), new MoveUsingEncoder(m_driveTrain, 180, 0, maximumPower), // step 3
        new TurnToCompassHeading(90), new MoveUsingEncoder(m_driveTrain, 60, 90, maximumPower), // step 4
        new TurnToCompassHeading(0), new MoveUsingEncoder(m_driveTrain, 60, 0, maximumPower), // step 5
        new TurnToCompassHeading(270), new MoveUsingEncoder(m_driveTrain, 60, 270, maximumPower), // step 6
        new TurnToCompassHeading(180), new MoveUsingEncoder(m_driveTrain, 60, 180, maximumPower), // step 7
        new TurnToCompassHeading(90), new MoveUsingEncoder(m_driveTrain, 60, 90, maximumPower), // step 8
        new TurnToCompassHeading(180), new MoveUsingEncoder(m_driveTrain, 180, 180, maximumPower), // tep 9
        new TurnToCompassHeading(270), new MoveUsingEncoder(m_driveTrain, 60, 270, maximumPower), // step 10
        new TurnToCompassHeading(180), new MoveUsingEncoder(m_driveTrain, 60, 180, maximumPower))); // step 11
  }
}