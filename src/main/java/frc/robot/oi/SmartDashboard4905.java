/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.oi;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Config4905;
import frc.robot.commands.ConfigReload;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.RunWheels;
import frc.robot.commands.ToggleLimelightLED;
import frc.robot.commands.pidcommands.MoveUsingEncoderTester;
import frc.robot.commands.pidcommands.TurnToCompassHeadingTester;
import frc.robot.groupcommands.athomechallengepathways.AtHomeChallengePoints;
import frc.robot.groupcommands.athomechallengepathways.BarrelRacingPath;
import frc.robot.groupcommands.athomechallengepathways.BouncePath;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathA;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathAAllSixBalls;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathB;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathBAllSixBalls;
import frc.robot.groupcommands.athomechallengepathways.HyperDriveChallenge;
import frc.robot.groupcommands.athomechallengepathways.InterstellarAccuracyChallenge;
import frc.robot.groupcommands.athomechallengepathways.PowerPortStart;
import frc.robot.groupcommands.athomechallengepathways.SlalomPath;
import frc.robot.groupcommands.athomechallengepathways.TestPath;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.pathgeneration.pathgenerators.DriveTrainDiagonalPathGenerator;
import frc.robot.pathgeneration.pathgenerators.TwoDDriveTrainPathGenerator;
import frc.robot.pidcontroller.RamseteCommand4905;
import frc.robot.pidcontroller.TracingPIDController;
import frc.robot.sensors.SensorsContainer;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.telemetries.Trace;
import frc.robot.telemetries.TracePair;

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

    SmartDashboard.putData("Shoot 10 feet",
        new ShootWithDistance(subsystemsContainer.getShooter(), subsystemsContainer.getFeeder(), 120));

    SmartDashboard.putData("Enable Limelight LEDs", new ToggleLimelightLED(true, sensorsContainer));

    SmartDashboard.putData("Disable Limelight LEDs", new ToggleLimelightLED(false, sensorsContainer));

    AutoModes4905.initializeAutoChooser(subsystemsContainer, sensorsContainer, m_autoChooser);

    SmartDashboard.putData("Galactic Search Path A All Six Balls",
        new GalacticSearchPathAAllSixBalls(subsystemsContainer.getDrivetrain(), subsystemsContainer.getIntake()));

    SmartDashboard.putData("Galactic Search Path A", new GalacticSearchPathA());

    SmartDashboard.putData("Galactic Search Path B All Six Balls",
        new GalacticSearchPathBAllSixBalls(subsystemsContainer.getDrivetrain(), subsystemsContainer.getIntake()));

    SmartDashboard.putData("Galactic Search Path B", new GalacticSearchPathB());
    SmartDashboard.putData("InterstellerAccuracyChallenge", new InterstellarAccuracyChallenge(
        subsystemsContainer.getDrivetrain(), subsystemsContainer.getShooter(), subsystemsContainer.getFeeder()));
    SmartDashboard.putData("Bounce Path", new BouncePath(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("Slalom Path", new SlalomPath(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("Barrel Racing Path", new BarrelRacingPath(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("Hyper Drive Challenge", new HyperDriveChallenge(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("Power Port Start", new PowerPortStart(subsystemsContainer.getDrivetrain(),
        subsystemsContainer.getShooter(), subsystemsContainer.getFeeder(), subsystemsContainer.getIntake()));

    DriveTrainDiagonalPathGenerator driveTrainDiagonalPathGenerator = new DriveTrainDiagonalPathGenerator(
        new TestPath(), subsystemsContainer.getDrivetrain(), AtHomeChallengePoints.E3);
    SmartDashboard.putData("Drive Test Path", driveTrainDiagonalPathGenerator.getPath());

    

    CommandBase barrelRacing = (new TwoDDriveTrainPathGenerator("BarrelRacing.wpilib.json", subsystemsContainer.getDrivetrain())).getPath();

    SmartDashboard.putData("Barrel Racing", barrelRacing);

    SmartDashboard.putData("RunDriveTrainWheels", new RunWheels(subsystemsContainer.getDrivetrain()));

  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }
}
