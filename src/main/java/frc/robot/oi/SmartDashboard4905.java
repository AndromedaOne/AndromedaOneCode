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
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.DoNothingAuto;
import frc.robot.commands.DriveBackwardTimed;
import frc.robot.commands.ExtendIntake;
import frc.robot.commands.RunIntake;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.pidcommands.MoveUsingEncoder;
import frc.robot.commands.pidcommands.TurnDeltaAngle;
import frc.robot.commands.pidcommands.TurnToCompassHeading;
import frc.robot.groupcommands.sequentialgroup.DelayedSequentialCommandGroup;
import frc.robot.subsystems.SubsystemsContainer;
import frc.robot.subsystems.drivetrain.DriveTrain;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterBase;

/**
 * Add your docs here.
 */
public class SmartDashboard4905 {
  SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  public SmartDashboard4905(SubsystemsContainer subsystemsContainer) {

    SmartDashboard.putData("DriveBackward", new DriveBackwardTimed(3, subsystemsContainer.getDrivetrain()));
    SmartDashboard.putData("MoveUsingEncoder", new MoveUsingEncoder(subsystemsContainer.getDrivetrain(), 48));

    SmartDashboard.putData("North", new TurnToCompassHeading(0));
    SmartDashboard.putData("South", new TurnToCompassHeading(180));
    SmartDashboard.putData("East", new TurnToCompassHeading(90));
    SmartDashboard.putData("West", new TurnToCompassHeading(270));
    SmartDashboard.putData("Turn -45", new TurnDeltaAngle(-45));
    SmartDashboard.putData("Turn -90", new TurnDeltaAngle(-90));
    SmartDashboard.putData("Turn -180", new TurnDeltaAngle(-180));
    SmartDashboard.putData("Turn 45", new TurnDeltaAngle(45));
    SmartDashboard.putData("Turn 90", new TurnDeltaAngle(90));
    SmartDashboard.putData("Turn 180", new TurnDeltaAngle(180));

    SmartDashboard.putNumber("Auto Delay", 0);

    initializeAutoChooser(subsystemsContainer);
  }

  private void initializeAutoChooser(SubsystemsContainer subsystemsContainer) {
    DriveTrain driveTrain = subsystemsContainer.getDrivetrain();
    ShooterBase shooter = subsystemsContainer.getShooter();
    IntakeBase intake = subsystemsContainer.getIntake();

    // @formatter:off
    m_autoChooser.setDefaultOption("DoNothing", 
                                   new DoNothingAuto());
    m_autoChooser.addOption("1: Move Back",
                            new DelayedSequentialCommandGroup(new MoveUsingEncoder(driveTrain, -12)));
    m_autoChooser.addOption("2: Fire and Move Back",
                            new DelayedSequentialCommandGroup(new ShooterCommand(shooter, 3),
                                                              new MoveUsingEncoder(driveTrain, -12)));
    m_autoChooser.addOption("3: Back Bumper U-Turn", 
                            new DelayedSequentialCommandGroup(new ShooterCommand(shooter, 3),
                                                              new MoveUsingEncoder(driveTrain, 30), // new WaitCommand(1.5),
                                                              new TurnToCompassHeading(270), // new WaitCommand(1.5),
                                                              new MoveUsingEncoder(driveTrain, 60),// new WaitCommand(1.5),
                                                              new TurnToCompassHeading(180),// new WaitCommand(1.5),
                                                              new MoveUsingEncoder(driveTrain, 126)));
    m_autoChooser.addOption("4: Shoot and Trench Run", 
                            new DelayedSequentialCommandGroup(new TurnToCompassHeading(334.5),
                                                              new ShooterCommand(shooter, 3),
                                                              new ExtendIntake(intake),
                                                              new TurnToCompassHeading(180),
                                                              new RunIntake(intake),
                                                              new MoveUsingEncoder(driveTrain, 249)));
    m_autoChooser.addOption("5: Right Side Shield",
                            new DelayedSequentialCommandGroup(new ShooterCommand(shooter, 3),
                                                              new MoveUsingEncoder(driveTrain, -69),
                                                              new ExtendIntake(intake),
                                                              new TurnToCompassHeading(270),
                                                              new RunIntake(intake),
                                                              new MoveUsingEncoder(driveTrain, 0))); // Waiting on official distance to move here from R&S
    m_autoChooser.addOption("6: Left Side Shield", 
                            new DelayedSequentialCommandGroup(new ShooterCommand(shooter, 3),
                                                              new MoveUsingEncoder(driveTrain, 24),
                                                              new TurnToCompassHeading(270),
                                                              new MoveUsingEncoder(driveTrain, 36),
                                                              new ExtendIntake(intake),
                                                              new TurnToCompassHeading(180),
                                                              new RunIntake(intake),
                                                              new MoveUsingEncoder(driveTrain, 102)));
    m_autoChooser.addOption("7: Enemy Trench Run", 
                            new DelayedSequentialCommandGroup(new ExtendIntake(intake),
                                                              new TurnToCompassHeading(180),
                                                              new RunIntake(intake),
                                                              new MoveUsingEncoder(driveTrain, 239)));
    SmartDashboard.putData("autoModes", m_autoChooser);
    // @formatter:on
  }

  public Command getSelectedAutoChooserCommand() {
    return m_autoChooser.getSelected();
  }
}
