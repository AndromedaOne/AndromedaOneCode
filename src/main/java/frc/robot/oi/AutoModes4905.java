package frc.robot.oi;

import java.util.function.DoubleSupplier;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Config4905;
import frc.robot.commands.DefaultFeederCommand;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.commands.DoNothingAuto;
import frc.robot.commands.SetGyroAdjustment;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.DriveAndIntake;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.groupcommands.sequentialgroup.ShootWithLimeLight;
import frc.robot.sensors.SensorsContainer;
import frc.robot.sensors.limelightcamera.LimeLightCameraBase;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer, SensorsContainer sensorsContainer,
      SendableChooser<Command> autoChooser) {
    m_autoChooser = autoChooser;
    Config driveTrainConfig = Config4905.getConfig4905().getDrivetrainConfig();
    double maxSpeedToPickupPowerCells = 0;
    DriveTrain drivetrain = subsystemsContainer.getDrivetrain();
    ShooterBase shooter = subsystemsContainer.getShooter();
    IntakeBase intake = subsystemsContainer.getIntake();
    FeederBase feeder = subsystemsContainer.getFeeder();
    LimeLightCameraBase limelight = sensorsContainer.getLimeLight();
    DoubleSupplier limelightHorizontalDegrees = limelight::horizontalDegreesToTarget;

    if (driveTrainConfig.hasPath("maxSpeedToPickupPowerCells")) {
      maxSpeedToPickupPowerCells = driveTrainConfig.getDouble("maxSpeedToPickupPowerCells");
    }

    // @formatter:off
        m_autoChooser.setDefaultOption("DoNothing", 
                                       new DoNothingAuto());
        m_autoChooser.addOption("1: Move Back",
                                new DelayedSequentialCommandGroup(new SetGyroAdjustment(0),
                                                                  new MoveUsingEncoder(drivetrain, (-1*12))));

        m_autoChooser.addOption("2: Fire and Move Back",
                                new DelayedSequentialCommandGroup(new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(drivetrain, (-1*12))));

        m_autoChooser.addOption("4: Shoot and Trench Run", 
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(334.5),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithDistance(shooter, feeder, (11.5*12)),
                                                                  new ParallelDeadlineGroup(
                                                                    new SequentialCommandGroup(
                                                                      new TurnToCompassHeading(180),
                                                                      new DriveAndIntake(drivetrain, intake, (14.5*12), maxSpeedToPickupPowerCells),
                                                                      new TurnToCompassHeading(351),
                                                                      new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                      new ShootWithLimeLight(shooter, feeder, limelight))), 
                                                                    new DefaultFeederCommand())
                                                                  );
                                                                  
        m_autoChooser.addOption("7: Enemy Trench Run (WARNING: EXTREMELY RISKY, DO NOT SELECT UNLESS 100% CONFIDENT)", 
                                new DelayedSequentialCommandGroup(new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(drivetrain, (23*12) + 9, maxSpeedToPickupPowerCells)));

        m_autoChooser.addOption("8: Right Fire Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(350.5),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(drivetrain, (-1*12))));

        m_autoChooser.addOption("9: Left Fire Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(9.5),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(drivetrain, (-1*12))));

        m_autoChooser.addOption("10: Far Left Fire Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(16),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(drivetrain, (-2*12))));

        // TODO 0.001 = temp value, negative = backwards or left
        m_autoChooser.addOption("11: Left 5-Ball", 
                                new DelayedSequentialCommandGroup(new DriveAndIntake(drivetrain, intake, 0.001, maxSpeedToPickupPowerCells),
                                                                  new TurnToCompassHeading(-0.001),
                                                                  new MoveUsingEncoder(drivetrain, 0.001),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight)));

        m_autoChooser.addOption("12: Left 8-Ball", 
                                new DelayedSequentialCommandGroup(new DriveAndIntake(drivetrain, intake, 0.001, maxSpeedToPickupPowerCells),
                                                                  new TurnToCompassHeading(-0.001),
                                                                  new MoveUsingEncoder(drivetrain, 0.001),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new TurnToCompassHeading(180),
                                                                  new DriveAndIntake(drivetrain, intake, 0.001, maxSpeedToPickupPowerCells),
                                                                  new TurnToCompassHeading(0),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight)));

        m_autoChooser.addOption("13: 8-Ball Trench/SG", 
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(334),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithDistance(shooter, feeder, (11.5*12)),
                                                                  new TurnToCompassHeading(180),
                                                                  new DriveAndIntake(drivetrain, intake, (14.5*12)),
                                                                  new TurnToCompassHeading(-0.001),
                                                                  new DriveAndIntake(drivetrain, intake, 0.001, maxSpeedToPickupPowerCells),
                                                                  new MoveUsingEncoder(drivetrain, -0.001),
                                                                  new TurnToCompassHeading(0.001),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight)));
        m_autoChooser.addOption("14: 8-Ball Home Trench", 
                                new DelayedSequentialCommandGroup(
                                                                  new TurnToCompassHeading(334.5),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithDistance(shooter, feeder, (11.5*12)),
                                                                  new TurnToCompassHeading(180),
                                                                  new DriveAndIntake(drivetrain, intake, (18*12), maxSpeedToPickupPowerCells),
                                                                  new TurnToCompassHeading(351),
                                                                  new TurnToFaceCommand(limelightHorizontalDegrees),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight)));
        m_autoChooser.addOption("15: 5-Ball Center", 
                                new DelayedSequentialCommandGroup(
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(drivetrain, (-5*12) - 9),
                                                                  new TurnToCompassHeading(270),
                                                                  new DriveAndIntake(drivetrain, intake, (1*12), maxSpeedToPickupPowerCells),
                                                                  new MoveUsingEncoder(drivetrain, (-1*12)),
                                                                  new TurnToCompassHeading(0),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight)));
        // @formatter:on
    SmartDashboard.putData("Auto Modes", m_autoChooser);
  }

}