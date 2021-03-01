package frc.robot.oi;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Config4905;
import frc.robot.commands.DeployAndRunIntake;
import frc.robot.commands.DoNothingAuto;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.commands.pidcommands.TurnToFaceCommand;
import frc.robot.groupcommands.athomechallengepathways.BarrelRacingPath;
import frc.robot.groupcommands.athomechallengepathways.BouncePath;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathA;
import frc.robot.groupcommands.athomechallengepathways.GalacticSearchPathB;
import frc.robot.groupcommands.athomechallengepathways.SlalomPath;
import frc.robot.groupcommands.parallelgroup.DriveAndIntake;
import frc.robot.groupcommands.parallelgroup.ShootWithDistance;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.groupcommands.sequentialgroup.ShootWithLimeLight;
import frc.robot.pathgeneration.pathgenerators.TwoDDriveTrainPathGenerator;
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
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    ShooterBase shooter = subsystemsContainer.getShooter();
    IntakeBase intake = subsystemsContainer.getIntake();
    FeederBase feeder = subsystemsContainer.getFeeder();
    LimeLightCameraBase limelight = sensorsContainer.getLimeLight();
    double maximumPower = 0.5;
    double robotLengthInches = 38;

    if (driveTrainConfig.hasPath("maxSpeedToPickupPowerCells")) {
      maxSpeedToPickupPowerCells = driveTrainConfig.getDouble("maxSpeedToPickupPowerCells");
    }

    // @formatter:off
        m_autoChooser.setDefaultOption("DoNothing", 
                                       new DoNothingAuto());
        m_autoChooser.addOption("1: Move Back",
                                new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, (-1*12))));
        m_autoChooser.addOption("2: Fire and Move Back",
                                new DelayedSequentialCommandGroup(new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(driveTrain, (-1*12))));
        m_autoChooser.addOption("3: Back Bumper U-Turn", 
                                new DelayedSequentialCommandGroup(new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(driveTrain, (2*12) + 6),
                                                                  new TurnToCompassHeading(270),
                                                                  new MoveUsingEncoder(driveTrain, (5*12)),
                                                                  new TurnToCompassHeading(180),
                                                                  new MoveUsingEncoder(driveTrain, (10*12) + 6)));
        m_autoChooser.addOption("4: Shoot and Trench Run", 
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(334.5),
                                                                  new TurnToFaceCommand(sensorsContainer.getLimeLight()::horizontalDegreesToTarget),
                                                                  new ShootWithDistance(shooter, feeder, (12*11.5)), // do math to figure out distance here
                                                                  new TurnToCompassHeading(180),
                                                                  new DriveAndIntake(driveTrain, intake, (11.0*12), maxSpeedToPickupPowerCells),
                                                                  new TurnToCompassHeading(351),
                                                                  new TurnToFaceCommand(limelight::horizontalDegreesToTarget),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight)));
        m_autoChooser.addOption("5: Right Side Shield",
                                new DelayedSequentialCommandGroup(new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(driveTrain, (-5*12) - 9),
                                                                  new TurnToCompassHeading(270),
                                                                  new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, (1*12), maxSpeedToPickupPowerCells))); // Waiting on official distance to move here from R&S
        m_autoChooser.addOption("6: Left Side Shield", 
                                new DelayedSequentialCommandGroup(new ShootWithLimeLight(shooter, feeder, limelight),
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
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(driveTrain, (-1*12))));
        m_autoChooser.addOption("9: Left Fire Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(9.5),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(driveTrain, (-1*12))));
        m_autoChooser.addOption("10: Right Fire Further Turn Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(344),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(driveTrain, (-2*12))));
        m_autoChooser.addOption("11: Left Fire Further Turn Move Back",
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(16),
                                                                  new TurnToFaceCommand(sensorsContainer.getLimeLight()::horizontalDegreesToTarget),
                                                                  new ShootWithLimeLight(shooter, feeder, limelight),
                                                                  new MoveUsingEncoder(driveTrain, (-2*12)))); 
        m_autoChooser.addOption("13: galactic Search Path A", new GalacticSearchPathA());
        m_autoChooser.addOption("14: galactic Search Path B", new GalacticSearchPathB());
        m_autoChooser.addOption("15: AutoNav: Barrel", new BarrelRacingPath(driveTrain));
        m_autoChooser.addOption("16: AutoNav: Slalom", new SlalomPath(driveTrain));
        CommandBase bounce1 = (new TwoDDriveTrainPathGenerator("BouncePart1.wpilib.json",
        subsystemsContainer.getDrivetrain(), "BounceP1")).getPath();
        CommandBase bounce2 = (new TwoDDriveTrainPathGenerator("BouncePart2.wpilib.json",
            subsystemsContainer.getDrivetrain(), false, "BounceP2")).getPath();
        CommandBase bounce3 = (new TwoDDriveTrainPathGenerator("BouncePart3.wpilib.json",
            subsystemsContainer.getDrivetrain(), false, "BounceP3")).getPath();
        CommandBase bounce4 = (new TwoDDriveTrainPathGenerator("BouncePart4.wpilib.json",
            subsystemsContainer.getDrivetrain(), false, "BounceP4")).getPath();

        SequentialCommandGroup fullBounce = new SequentialCommandGroup(bounce1, bounce2, bounce3, bounce4);
        m_autoChooser.addOption("17: AutoNav: Bounce", fullBounce);

                                                                
          SmartDashboard.putData("autoModes", m_autoChooser);
    // @formatter:on
  }

}