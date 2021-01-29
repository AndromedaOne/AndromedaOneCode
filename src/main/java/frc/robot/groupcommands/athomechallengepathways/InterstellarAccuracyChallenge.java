/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.groupcommands.athomechallengepathways;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;
import frc.robot.commands.WaitToLoad;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.shooter.ShooterBase;
// NOTE: Back button ends the wait.
//1. Shoot all balls at Green distance
//2. Drive backward to Reload
//3. Wain until drive input
//4. Drive forward to yellow
//5. Shoot
//6. Drive backward to reload
//7. wait
//8. Drive to blue
//9. Shoot
//10. Back to reload
//11. wait
//12. Drive to red
//13. Shoot
//14. Drive backward to reload
//15. wait
//16. Drive to blue
//17. Shoot

public class InterstellarAccuracyChallenge extends SequentialCommandGroup {
  /**
   * Creates a new InterstellarAccuracyChallenge.
   */
  private final double m_maxOutPut = 0.5;
  private final double greenToReload = 220;
  private final double reloadToYellow = 160;
  private final double reloadToBlue = 100;
  private final double reloadToRed = 40;

  public InterstellarAccuracyChallenge(DriveTrain driveTrain, ShooterBase shooter, FeederBase feeder) {

    addCommands(new DelayedSequentialCommandGroup(
        // 1. start at 70 inches
        new TurnToFaceCommand(Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget),
        new ShootWithDistance(shooter, feeder, 0),
        // 2.
        new MoveUsingEncoder(driveTrain, -greenToReload, 180, m_maxOutPut)));
        // // 3.
        // new WaitToLoad(driveTrain),
        // // 4.
        // new MoveUsingEncoder(driveTrain, reloadToYellow, 0, m_maxOutPut),
        // // 5.
        // new TurnToFaceCommand(Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget),
        // new ShootWithDistance(shooter, feeder, 0),
        // // 6.
        // new MoveUsingEncoder(driveTrain, -reloadToYellow, 180, m_maxOutPut),
        // // 7.
        // new WaitToLoad(driveTrain),
        // // 8.
        // new MoveUsingEncoder(driveTrain, reloadToBlue, 0, m_maxOutPut),
        // // 9.
        // new TurnToFaceCommand(Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget),
        // new ShootWithDistance(shooter, feeder, 0),
        // // 10.
        // new MoveUsingEncoder(driveTrain, -reloadToBlue, 180, m_maxOutPut),
        // // 11.
        // new WaitToLoad(driveTrain),
        // // 12.
        // new MoveUsingEncoder(driveTrain, reloadToRed, 0, m_maxOutPut),
        // // 13.
        // new TurnToFaceCommand(Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget),
        // new ShootWithDistance(shooter, feeder, 0),
        // // 14.
        // new MoveUsingEncoder(driveTrain, -reloadToRed, 180, m_maxOutPut),
        // // 15.
        // new WaitToLoad(driveTrain),
        // // 16.
        // new MoveUsingEncoder(driveTrain, reloadToBlue, 0, m_maxOutPut),
        // // 17.
        // new TurnToFaceCommand(Robot.getInstance().getSensorsContainer().getLimeLight()::horizontalDegreesToTarget),
        // new ShootWithDistance(shooter, feeder, 0)));

  }
}
