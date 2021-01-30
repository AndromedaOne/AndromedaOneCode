/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.ConfigReload;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.RunAllFeederMotors;
import frc.robot.commands.ToggleLimelightLED;
import frc.robot.commands.pidcommands.MoveUsingEncoderTester;
import frc.robot.commands.pidcommands.RunShooterWheelVelocity;
import frc.robot.commands.pidcommands.TurnToCompassHeadingTester;
import frc.robot.groupcommands.athomechallengepathways.AtHomeChallengePoints;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathA;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathB;
import frc.robot.groupcommands.athomechallengepathways.TestPath;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.groupcommands.parallelgroup.ShootWithRPM;
import frc.robot.pathgeneration.pathgenerators.DriveTrainDiagonalPathGenerator;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;

/**
 * Add your docs here.
 */
public class SmartDashboard4905 {
  SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  public SmartDashboard4905(SubsystemsContainer subsystemsContainer, SensorsContainer sensorsContainer) {

    SmartDashboard.putData("DriveBackward", new DriveBackwardTimed(1, subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("MoveUsingEncoderTester", new MoveUsingEncoderTester(subsystemsContainer.getDrivetrain()));

    SmartDashboard.putData("TurnToCompassHeadingTester",
        new TurnToCompassHeadingTester(SmartDashboard.getNumber("Compass Heading", 0)));

    SmartDashboard.putNumber("Auto Delay", 0);

    SmartDashboard.putData("Reload Config", new ConfigReload());

    SmartDashboard.putData("ShootRPM",
        new ShootWithRPM(subsystemsContainer.getShooter(), subsystemsContainer.getFeeder(), 4700));

    SmartDashboard.putData("Enable Limelight LEDs", new ToggleLimelightLED(true, sensorsContainer));

    SmartDashboard.putData("Disable Limelight LEDs", new ToggleLimelightLED(false, sensorsContainer));

    AutoModes4905.initializeAutoChooser(subsystemsContainer, sensorsContainer, m_autoChooser);

    SmartDashboard.putData("Galactic Search Path A",
        new GalacticSearchPathA(subsystemsContainer.getDrivetrain(), subsystemsContainer.getIntake()));

    SmartDashboard.putData("Galactic Search Path B",
        new GalacticSearchPathB(subsystemsContainer.getDrivetrain(), subsystemsContainer.getIntake()));

    DriveTrainDiagonalPathGenerator driveTrainDiagonalPathGenerator = new DriveTrainDiagonalPathGenerator(
        new TestPath(), subsystemsContainer.getDrivetrain(), AtHomeChallengePoints.E3);
    SmartDashboard.putData("Drive Test Path", driveTrainDiagonalPathGenerator.getPath());

    SmartDashboard.putData("Run All feeder motors",
        new RunAllFeederMotors(subsystemsContainer.getFeeder()));
  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }
}
