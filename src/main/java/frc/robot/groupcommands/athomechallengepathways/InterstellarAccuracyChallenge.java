/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.commands.*;
import frc.robot.commands.WaitToLoad;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.ShootWithRPM;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class InterstellarAccuracyChallenge extends SequentialCommandGroup {
  /**
   * Creates a new InterstellarAccuracyChallenge.
   */
  private final double m_maxOutPut = 0.5;
  private final double reloadToGreen = 220;
  private final double reloadToYellow = 160;
  private final double reloadToBlue = 100;
  private final double reloadToRed = 57;

  public InterstellarAccuracyChallenge(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder) {

    addCommands(new DelayedSequentialCommandGroup(
        // 1. start at 70 inches
        new ShootWithRPM(shooter, feeder,
            Config4905.getConfig4905().getShooterConfig().getDouble("shootingrpm.backOfGreenZone")),  
            new WaitToLoad(driveTrain),
        // 2.
        new ParallelDeadlineGroup(
            new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, -reloadToGreen, 1, m_maxOutPut),
                new WaitToLoad(driveTrain), new MoveUsingEncoder(driveTrain, reloadToYellow, -1, m_maxOutPut),
                new TurnToFaceCommand(
                    Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget)),
            new DefaultFeederCommand()),

        new ShootWithRPM(shooter, feeder,
            Config4905.getConfig4905().getShooterConfig().getDouble("shootingrpm.centerOfYellowZone")),

        new ParallelDeadlineGroup(
            new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, -reloadToYellow, 1, m_maxOutPut),
                new WaitToLoad(driveTrain), new MoveUsingEncoder(driveTrain, reloadToBlue, -1, m_maxOutPut),
                new TurnToFaceCommand(
                    Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget)),
            new DefaultFeederCommand()),

        new ShootWithRPM(shooter, feeder,
            Config4905.getConfig4905().getShooterConfig().getDouble("shootingrpm.centerOfBlueZone")),

        new ParallelDeadlineGroup(
            new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, -reloadToBlue - 5, 1, m_maxOutPut),
                new WaitToLoad(driveTrain), new MoveUsingEncoder(driveTrain, reloadToRed, -1, m_maxOutPut),
                new TurnToFaceCommand(
                    Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget)),
            new DefaultFeederCommand()),

        new ShootWithRPM(shooter, feeder,
            Config4905.getConfig4905().getShooterConfig().getDouble("shootingrpm.frontOfRedZone")),

        new ParallelDeadlineGroup(
            new SequentialCommandGroup(new MoveUsingEncoder(driveTrain, -reloadToRed, 1, m_maxOutPut),
                new WaitToLoad(driveTrain), new MoveUsingEncoder(driveTrain, reloadToYellow, -1, m_maxOutPut),
                new TurnToFaceCommand(
                    Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget)),
            new DefaultFeederCommand()),

        new ShootWithRPM(shooter, feeder,
            Config4905.getConfig4905().getShooterConfig().getDouble("shootingrpm.centerOfYellowZone"))));

  }

  public void initialize() {
    super.initialize();
    Trace.getInstance().logCommandStart(this);
  }

  public void end(boolean interrupted) {
    super.end(interrupted);
    Trace.getInstance().logCommandStop(this);
  }
}
