// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.topGunShooterFeederCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.topGunFeederCommands.RunFeeder;
import frc.robot.commands.topGunIntakeCommands.DeployAndRunIntake;
import frc.robot.commands.topGunShooterCommands.InitializeShooterAlignment;
import frc.robot.commands.topGunShooterCommands.MoveShooterAlignment;
import frc.robot.commands.topGunShooterCommands.RunShooterRPM;
import frc.robot.rewrittenWPIclasses.SequentialCommandGroup4905;
import frc.robot.subsystems.topGunFeeder.FeederBase;
import frc.robot.subsystems.topGunIntake.IntakeBase;
import frc.robot.subsystems.topGunShooter.ShooterAlignmentBase;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;

public class PickUpCargo extends SequentialCommandGroup4905 {

  public PickUpCargo(FeederBase feeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, ShooterAlignmentBase shooterAlignment,
      IntakeBase intakeBase, boolean reverse) {

    final double m_shooterSetpoint = -1000.0;
    final double m_feederSetpoint = 0.5; // do we want to run full speed?
    final double m_shooterAngle = 2.0;
    // m_feederReverseState is used to let the feeder subsystem know to negate the
    // setpoint
    boolean m_feederReverseState = false;
    RunShooterRPM runShooterCommand;

    // PickUpCargo - this takes in cargo from floor as well as push cargo out
    // through the intake.
    // 6th parameter reverse is true when pushing out cargo through intake.
    // When true, shooter setpoint and feeder setoint will be negative.

    if (reverse) {
      runShooterCommand = new RunShooterRPM(topShooterWheel, bottomShooterWheel,
          () -> -m_shooterSetpoint);
      m_feederReverseState = false;
    } else {
      runShooterCommand = new RunShooterRPM(topShooterWheel, bottomShooterWheel,
          () -> m_shooterSetpoint);
      m_feederReverseState = true;
    }

    addCommands(new InitializeShooterAlignment(shooterAlignment),
        new MoveShooterAlignment(shooterAlignment, () -> m_shooterAngle),
        new ParallelCommandGroup(runShooterCommand,
            new RunFeeder(feeder, () -> m_feederSetpoint, m_feederReverseState, () -> false),
            new DeployAndRunIntake(intakeBase, reverse)));
  }

}