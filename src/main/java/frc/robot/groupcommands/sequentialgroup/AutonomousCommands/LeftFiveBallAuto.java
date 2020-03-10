/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.sequentialgroup.AutonomousCommands;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DefaultFeederCommand;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.commands.SetGyroAdjustment;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.DriveAndIntake;
import frc.robot.groupcommands.parallelgroup.ShooterParallelSetShooterVelocity;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.groupcommands.sequentialgroup.ShootWithLimeLight;
import frc.robot.oi.AutoSubsystemsAndParameters;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LeftFiveBallAuto extends DelayedSequentialCommandGroup {
  /**
   * Creates a new LeftFiveBallAuto.
   */
  AutoSubsystemsAndParameters m_autoSubsystemsAndParameters;
  private final double angleToTurnToForPickingUpSecondPowerCell = 240;
  private final double angleToTurnToForDrivingToShootingPosition = angleToTurnToForPickingUpSecondPowerCell;

  public LeftFiveBallAuto(AutoSubsystemsAndParameters autoSubsystemsAndParameters) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
    m_autoSubsystemsAndParameters = autoSubsystemsAndParameters;

    addCommands(
      new SetGyroAdjustment(180),
      new ParallelDeadlineGroup(
        getCommandsToDriveToBallsAndThenShoot(), 
        new DeployAndRunIntake(m_autoSubsystemsAndParameters.getIntake(), () -> false)
      )
    );
  }

  private SequentialCommandGroup getCommandsToDriveToBallsAndThenShoot() {
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(
        getCommandsToDriveUpToBallsAndThenDriveToShootingPosition(), 
        new DefaultFeederCommand(),
        new ShooterParallelSetShooterVelocity(m_autoSubsystemsAndParameters.getShooter(), 3500, 3500)
      ),
      new ShootWithLimeLight(m_autoSubsystemsAndParameters.getShooter(), m_autoSubsystemsAndParameters.getFeeder(), m_autoSubsystemsAndParameters.getLimelight())
    );
  }

  private SequentialCommandGroup getCommandsToDriveUpToBallsAndThenDriveToShootingPosition() {
    return new SequentialCommandGroup(
      new MoveUsingEncoder(m_autoSubsystemsAndParameters.getDriveTrain(), (6.0 * 12), m_autoSubsystemsAndParameters.getMaxSpeedToPickupPowerCells(), 180),
      new TurnToCompassHeading(angleToTurnToForPickingUpSecondPowerCell), 
      new MoveUsingEncoder(m_autoSubsystemsAndParameters.getDriveTrain(), (0.5 * 12), m_autoSubsystemsAndParameters.getMaxSpeedToPickupPowerCells(), angleToTurnToForPickingUpSecondPowerCell),
      new MoveUsingEncoder(m_autoSubsystemsAndParameters.getDriveTrain(), -(10 * 12), 0, angleToTurnToForDrivingToShootingPosition),
      new TurnToCompassHeading(45),
      new TurnToFaceCommand(m_autoSubsystemsAndParameters.getLimelight())
    );
  }
}
