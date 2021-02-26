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
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ConfigReload;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.RunWheels;
import frc.robot.commands.ToggleLimelightLED;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.MoveUsingEncoderTester;
import frc.robot.commands.pidcommands.TurnDeltaAngle;
import frc.robot.commands.pidcommands.TurnToCompassHeadingTester;
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
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.pathgeneration.pathgenerators.TwoDDriveTrainPathGenerator;
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
    SmartDashboard.putData("Bounce Path",
        new BouncePath(subsystemsContainer.getDrivetrain(), subsystemsContainer.getIntake()));
    SmartDashboard.putData("Slalom Path", new SlalomPath(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("Barrel Racing Path", new BarrelRacingPath(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("Hyper Drive Challenge", new HyperDriveChallenge(subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("Power Port Start", new PowerPortStart(subsystemsContainer.getDrivetrain(),
        subsystemsContainer.getShooter(), subsystemsContainer.getFeeder(), subsystemsContainer.getIntake()));


    CommandBase barrelRacing = (new TwoDDriveTrainPathGenerator("BarrelRacing.wpilib.json",
        subsystemsContainer.getDrivetrain(), "BarrelRacing")).getPath();

    SmartDashboard.putData("Barrel Racing 2d", barrelRacing);

    CommandBase slalom = (new TwoDDriveTrainPathGenerator("Slalom.wpilib.json", subsystemsContainer.getDrivetrain(), "Slalom"))
        .getPath();

    SmartDashboard.putData("Slalom 2d", slalom);

    CommandBase bounce1 = (new TwoDDriveTrainPathGenerator("BouncePart1.wpilib.json",
        subsystemsContainer.getDrivetrain(), "BounceP1")).getPath();
    CommandBase bounce2 = (new TwoDDriveTrainPathGenerator("BouncePart2.wpilib.json",
        subsystemsContainer.getDrivetrain(), false, "BounceP2")).getPath();
    CommandBase bounce3 = (new TwoDDriveTrainPathGenerator("BouncePart3.wpilib.json",
        subsystemsContainer.getDrivetrain(), false, "BounceP3")).getPath();
    CommandBase bounce4 = (new TwoDDriveTrainPathGenerator("BouncePart4.wpilib.json",
        subsystemsContainer.getDrivetrain(), false, "BounceP4")).getPath();

    SequentialCommandGroup fullBounce = new SequentialCommandGroup(bounce1, bounce2, bounce3, bounce4);

    SmartDashboard.putData("Bounce 2d", fullBounce);

    SmartDashboard.putData("RunDriveTrainWheels", new RunWheels(subsystemsContainer.getDrivetrain()));

    MoveUsingEncoder drive120Inches = new MoveUsingEncoder(subsystemsContainer.getDrivetrain(), 120, 0, 0.3);
    SmartDashboard.putData("drive120Inches", drive120Inches);

    MoveUsingEncoder drive36Inches = new MoveUsingEncoder(subsystemsContainer.getDrivetrain(), 36, 0, 0.3);
    TurnDeltaAngle turn90 = new TurnDeltaAngle(90);
    MoveUsingEncoder drive36Inchesbackwards = new MoveUsingEncoder(subsystemsContainer.getDrivetrain(), -36, 0, 0.3);


  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }
}
