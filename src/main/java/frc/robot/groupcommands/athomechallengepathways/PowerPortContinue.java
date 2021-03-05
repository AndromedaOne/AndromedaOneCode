/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
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
  private final double reloadToGreen = reIntroductionZoneDistance - greenZoneShootingDistance;

  public PowerPortContinue(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder, IntakeBase intake) {
    DoubleSupplier d = Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget;
    addCommands(new DelayedSequentialCommandGroup(
        new MoveUsingEncoder(driveTrain, reloadToGreen, true, () -> .5 * d.getAsDouble(), m_maxOutPut),
        new PowerPortStart(driveTrain, shooter, feeder, intake)));
  }
}
