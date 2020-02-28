package frc.robot.oi;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Config4905;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.commands.DoNothingAuto;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.parallelgroup.DriveAndIntake;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.sensors.SensorsContainer;
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
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    ShooterBase shooter = subsystemsContainer.getShooter();
    IntakeBase intake = subsystemsContainer.getIntake();
    FeederBase feeder = subsystemsContainer.getFeeder();

    if (driveTrainConfig.hasPath("maxSpeedToPickupPowerCells")) {
      maxSpeedToPickupPowerCells = driveTrainConfig.getDouble("maxSpeedToPickupPowerCells");
    }

    // @formatter:off
        m_autoChooser.setDefaultOption("DoNothing", 
                                       new DoNothingAuto());
        m_autoChooser.addOption("1: Move Back",
                                new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, (-1*12))));
        m_autoChooser.addOption("2: Fire and Move Back",
                                new DelayedSequentialCommandGroup(new ShootWithDistance(shooter, feeder, (10*12 + 3)),
                                                                  new MoveUsingEncoder(driveTrain, (-4.5*12), 0, -0.1),
                                                                  new TurnToCompassHeading(240),
                                                                  new DriveAndIntake(driveTrain, intake, 12),
                                                                  new MoveUsingEncoder(driveTrain, -12),
                                                                  new TurnToCompassHeading(357),
                                                                  new TurnToFaceCommand(sensorsContainer.getLimeLight()::horizontalDegreesToTarget),
                                                                  new ShootWithDistance(shooter, feeder, (21*12))
                                                                  ));
        m_autoChooser.addOption("3: Back Bumper U-Turn", 
                                new DelayedSequentialCommandGroup(new ShootWithDistance(shooter, feeder, (10*12)),
                                                                  new MoveUsingEncoder(driveTrain, (2*12) + 6),
                                                                  new TurnToCompassHeading(270),
                                                                  new MoveUsingEncoder(driveTrain, (5*12)),
                                                                  new TurnToCompassHeading(180),
                                                                  new MoveUsingEncoder(driveTrain, (10*12) + 6)));
        m_autoChooser.addOption("4: Shoot and Trench Run", 
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(334.5),
                                                                  new ShootWithDistance(shooter, feeder, 11*12), // do math to figure out distance here
                                                                  new TurnToCompassHeading(180),
                                                                  
                                                                  new DriveAndIntake(driveTrain, intake, (14.5*12), maxSpeedToPickupPowerCells),
                                                                  new TurnToCompassHeading(351),
                                                                  new ShootWithDistance(shooter, feeder, 26*12)));
        m_autoChooser.addOption("5: Right Side Shield",
                                new DelayedSequentialCommandGroup(new ShootWithDistance(shooter, feeder, (10*12)),
                                                                  new MoveUsingEncoder(driveTrain, (-5*12) - 9),
                                                                  new TurnToCompassHeading(270),
                                                                  new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, (1*12), maxSpeedToPickupPowerCells))); // Waiting on official distance to move here from R&S
        m_autoChooser.addOption("6: Left Side Shield", 
                                new DelayedSequentialCommandGroup(new ShootWithDistance(shooter, feeder, (10*12)),
                                                                  new MoveUsingEncoder(driveTrain, (2*12) + 6),
                                                                  new TurnToCompassHeading(270),
                                                                  new MoveUsingEncoder(driveTrain, (5*12)),
                                                                  new TurnToCompassHeading(180),
                                                                  new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, (9*12)), // Go most of the distance full speed, slow down at end to pickup power cells
                                                                  new MoveUsingEncoder(driveTrain, (1*12) + 6, maxSpeedToPickupPowerCells)));
        // Supposedly never going to be used, according to R&S, but kept to keep numbering system intact
        if (false) {
          m_autoChooser.addOption("7: Enemy Trench Run", 
                                new DelayedSequentialCommandGroup(new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, (23*12) + 9, maxSpeedToPickupPowerCells)));
        }
        m_autoChooser.addOption("8: Right Fire Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(350.5),
                                                                  new ShootWithDistance(shooter, feeder, (10*12)),
                                                                  new MoveUsingEncoder(driveTrain, (-1*12))));
        m_autoChooser.addOption("9: Left Fire Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(9.5),
                                                                  new ShootWithDistance(shooter, feeder, (10*12)),
                                                                  new MoveUsingEncoder(driveTrain, (-1*12))));
        m_autoChooser.addOption("10: Right Fire Further Turn Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(344),
                                                                  new ShootWithDistance(shooter, feeder, (11*12)),
                                                                  new MoveUsingEncoder(driveTrain, (-2*12))));
        m_autoChooser.addOption("11: Left Fire Further Turn Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(16),
                                                                  new ShootWithDistance(shooter, feeder, (11*12 )),
                                                                  new TurnToCompassHeading(0),
                                                                  new MoveUsingEncoder(driveTrain, (-4.5*12)),
                                                                  new TurnToCompassHeading(157.2),
                                                                  new DriveAndIntake(driveTrain, intake, 12),
                                                                  new MoveUsingEncoder(driveTrain, -12),
                                                                  new TurnToCompassHeading(8),
                                                                  new TurnToFaceCommand(sensorsContainer.getLimeLight()::horizontalDegreesToTarget),
                                                                  new ShootWithDistance(shooter, feeder, (18*12))));
        SmartDashboard.putData("autoModes", m_autoChooser);
        // @formatter:on
  }

}