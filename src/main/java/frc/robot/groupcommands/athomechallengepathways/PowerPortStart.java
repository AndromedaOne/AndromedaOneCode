/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.Robot;
import frc.robot.commands.DefaultFeederCommand;
import frc.robot.commands.RevUpShooter;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.ShootWithRPM;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class PowerPortStart extends PowerPortBase {
  /**
   * Creates a new PowerPortShoot.
   */
  private final double m_maxOutPut = 1;

  public PowerPortStart(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder, IntakeBase intake) {
    addCommands(
        new DelayedSequentialCommandGroup(
            new ParallelDeadlineGroup(
                new TurnToFaceCommand(
                    Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget),
                new RevUpShooter(shooter, getpowerPortShootingRPM())),
            new ShootWithRPM(shooter, feeder, getpowerPortShootingRPM()),
            new ParallelCommandGroup(new MoveUsingEncoder(driveTrain, -getpowerPortDistance(), 1.5, m_maxOutPut),
                new DefaultFeederCommand())));
  }
}
