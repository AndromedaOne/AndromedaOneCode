/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.sequentialgroup.AutonomousCommands;

import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.groupcommands.parallelgroup.DriveAndIntake;
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
  public CenterFiveBallAuto(AutoSubsystemsAndParameters autoSubsystemsAndParameters) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
    addCommands(
      new ShootWithLimeLight(autoSubsystemsAndParameters.getShooter(), autoSubsystemsAndParameters.getFeeder(), autoSubsystemsAndParameters.getLimelight()),
      new MoveUsingEncoder(autoSubsystemsAndParameters.getDriveTrain(), (-5*12) - 9),
      new TurnToCompassHeading(270),
      new DriveAndIntake(autoSubsystemsAndParameters.getDriveTrain(), autoSubsystemsAndParameters.getIntake(), (1*12), autoSubsystemsAndParameters.getMaxSpeedToPickupPowerCells()),
      new MoveUsingEncoder(autoSubsystemsAndParameters.getDriveTrain(), (-1*12)),
      new TurnToCompassHeading(0),
      new ShootWithLimeLight(autoSubsystemsAndParameters.getShooter(), autoSubsystemsAndParameters.getFeeder(), autoSubsystemsAndParameters.getLimelight())
      );
  }
}
