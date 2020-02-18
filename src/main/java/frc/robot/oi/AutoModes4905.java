package frc.robot.oi;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.DoNothingAuto;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.groupcommands.DeployAndRunIntake;
import frc.robot.groupcommands.parallelgroup.ShootWithRPM;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterBase;

public class AutoModes4905 {
  static SendableChooser<Command> m_autoChooser;

  public static void initializeAutoChooser(SubsystemsContainer subsystemsContainer,
      SendableChooser<Command> autoChooser) {
    m_autoChooser = autoChooser;
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    ShooterBase shooter = subsystemsContainer.getShooter();
    IntakeBase intake = subsystemsContainer.getIntake();
    FeederBase feeder = subsystemsContainer.getFeeder();

    // @formatter:off
        m_autoChooser.setDefaultOption("DoNothing", 
                                       new DoNothingAuto());
        m_autoChooser.addOption("1: Move Back",
                                new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, -12)));
        m_autoChooser.addOption("2: Fire and Move Back",
                                new DelayedSequentialCommandGroup(new ShootWithRPM(shooter, feeder, 1000),
                                                                  new MoveUsingEncoder(driveTrain, -12)));
        m_autoChooser.addOption("3: Back Bumper U-Turn", 
                                new DelayedSequentialCommandGroup(new ShooterCommand(shooter, 3),
                                                                  new MoveUsingEncoder(driveTrain, 30),
                                                                  new TurnToCompassHeading(270),
                                                                  new MoveUsingEncoder(driveTrain, 60),
                                                                  new TurnToCompassHeading(180),
                                                                  new MoveUsingEncoder(driveTrain, 126)));
        m_autoChooser.addOption("4: Shoot and Trench Run", 
                                new DelayedSequentialCommandGroup(new TurnToCompassHeading(334.5),
                                                                  new ShooterCommand(shooter, 3),
                                                                  new TurnToCompassHeading(180),
                                                                  new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, 249)));
        m_autoChooser.addOption("5: Right Side Shield",
                                new DelayedSequentialCommandGroup(new ShooterCommand(shooter, 3),
                                                                  new MoveUsingEncoder(driveTrain, -69),
                                                                  new TurnToCompassHeading(270),
                                                                  new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, 12))); // Waiting on official distance to move here from R&S
        m_autoChooser.addOption("6: Left Side Shield", 
                                new DelayedSequentialCommandGroup(new ShooterCommand(shooter, 3),
                                                                  new MoveUsingEncoder(driveTrain, (2*12)+6),
                                                                  new TurnToCompassHeading(270),
                                                                  new MoveUsingEncoder(driveTrain, (5*12)),
                                                                  new TurnToCompassHeading(180),
                                                                  new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, (10*12) + 6)));
        m_autoChooser.addOption("7: Enemy Trench Run", 
                                new DelayedSequentialCommandGroup(new DeployAndRunIntake(intake, () -> true),
                                                                  new MoveUsingEncoder(driveTrain, (23*12)+9)));
        SmartDashboard.putData("autoModes", m_autoChooser);
        // @formatter:on
  }

}