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
import frc.robot.groupcommands.parallelgroup.DriveAndIntake;
import frc.robot.groupcommands.parallelgroup.ShooterParallelSetShooterVelocity;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.groupcommands.sequentialgroup.ShootWithLimeLight;
import frc.robot.oi.AutoSubsystemsAndParameters;


// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class CenterFiveBallAuto extends DelayedSequentialCommandGroup {
  /**
   * Creates a new CenterFiveBallAuto.
   */
  AutoSubsystemsAndParameters m_autoSubsystemsAndParameters;
  public CenterFiveBallAuto(AutoSubsystemsAndParameters autoSubsystemsAndParameters) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
    m_autoSubsystemsAndParameters = autoSubsystemsAndParameters;
    new DeployAndRunIntake(autoSubsystemsAndParameters.getIntake(), () -> false);
    addCommands(
      new SetGyroAdjustment(180),
      new ParallelDeadlineGroup(
        getCommandsToDriveToBallsAndShoot(), 
        new DeployAndRunIntake(m_autoSubsystemsAndParameters.getIntake(), () -> false))
    );

  }

  private SequentialCommandGroup getCommandsToDriveToBallsAndShoot() {
    return new SequentialCommandGroup(
      new ParallelDeadlineGroup(
        getCommandsToGoGetTwoPowerCellsFromCenter(),
        new DefaultFeederCommand(),
        new ShooterParallelSetShooterVelocity(m_autoSubsystemsAndParameters.getShooter(), 3500, 3500)
        ),
      new ShootWithLimeLight(m_autoSubsystemsAndParameters.getShooter(), m_autoSubsystemsAndParameters.getFeeder(), m_autoSubsystemsAndParameters.getLimelight())
    );
  }

  private SequentialCommandGroup getCommandsToGoGetTwoPowerCellsFromCenter() {
    double angleToTurnToShieldGenerator = 270;
    return new SequentialCommandGroup(
      new MoveUsingEncoder(m_autoSubsystemsAndParameters.getDriveTrain(), (5*12) + 9, 0, 180),
      new TurnToCompassHeading(angleToTurnToShieldGenerator),
      new MoveUsingEncoder(m_autoSubsystemsAndParameters.getDriveTrain(), (1*12), m_autoSubsystemsAndParameters.getMaxSpeedToPickupPowerCells(), angleToTurnToShieldGenerator),
      new MoveUsingEncoder(m_autoSubsystemsAndParameters.getDriveTrain(), (-1*12), 0, angleToTurnToShieldGenerator),
      new TurnToCompassHeading(0)
    );
  }
}
