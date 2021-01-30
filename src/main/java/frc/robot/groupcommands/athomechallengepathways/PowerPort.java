/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class PowerPort extends SequentialCommandGroup {
  /**
   * Creates a new PowerPort.
   */
  private final double m_maxOutPut = 0.5;
  private final double reloadToGreen = 210;
  private final double greenZoneShootingDistance = 90;

  public PowerPort(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder) {
    addCommands(new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, reloadToGreen, 0, m_maxOutPut),
        new TurnToFaceCommand(Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget),
        new ShootWithDistance(shooter, feeder, greenZoneShootingDistance),
        new MoveUsingEncoder(driveTrain, -reloadToGreen, 0, m_maxOutPut)));
  }
}
