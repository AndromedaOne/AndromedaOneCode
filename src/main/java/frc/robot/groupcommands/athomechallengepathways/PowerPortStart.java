/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.DefaultFeederCommand;
import frc.robot.commands.RevUpShooter;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.ShootWithRPM;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.sensors.gyro.NavXGyroSensor;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class PowerPortStart extends SequentialCommandGroup {
  /**
   * Creates a new PowerPortShoot.
   */
  private final double m_maxOutPut = 1;
  private final double greenZoneShootingRPM = 1675;
  private final double reIntroductionZoneDistance = 330;
  private final double greenZoneShootingDistance = 180;
  private final double reloadToGreen = reIntroductionZoneDistance - greenZoneShootingDistance - 20;
  private NavXGyroSensor navX;

  public PowerPortStart(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder, IntakeBase intake) {
    navX = Robot.getInstance().getSensorsContainer().getNavXGyro();
    DoubleSupplier d = Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget;
    // @formatter:off
    addCommands(
        new DelayedSequentialCommandGroup(
            new ParallelDeadlineGroup(
               new TurnToFaceCommand(d),
               new RevUpShooter(shooter)),
            new ShootWithRPM(shooter, feeder, greenZoneShootingRPM),
            new ParallelCommandGroup(
                new MoveUsingEncoder(driveTrain, -reloadToGreen, 0, m_maxOutPut),
                new DefaultFeederCommand()))); 
    // @formatter:on
  }
}
