/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.RevUpShooter;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class PowerPortContinue extends SequentialCommandGroup {
  /**
   * Creates a new PowerPort.
   */
  private final double m_maxOutPut = 1;
  private final double greenZoneShootingDistance = 180;
  private final double reIntroductionZoneDistance = 330;
  private final double reloadToGreen = reIntroductionZoneDistance - greenZoneShootingDistance - 20;

  public PowerPortContinue(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder, IntakeBase intake) {
    addCommands(new DelayedSequentialCommandGroup(
        new ParallelDeadlineGroup(new MoveUsingEncoder(driveTrain, reloadToGreen, 0, m_maxOutPut),
            new RevUpShooter(shooter)),
        new PowerPortStart(driveTrain, shooter, feeder, intake)));
  }
}
