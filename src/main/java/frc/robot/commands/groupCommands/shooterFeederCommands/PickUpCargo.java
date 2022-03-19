// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feederCommands.RunFeeder;
import frc.robot.commands.intakeCommands.DeployAndRunIntake;
import frc.robot.commands.shooterCommands.InitializeShooterAlignment;
import frc.robot.commands.shooterCommands.MoveShooterAlignment;
import frc.robot.commands.shooterCommands.RunShooterRPM;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.subsystems.intake.IntakeBase;
import frc.robot.subsystems.shooter.ShooterAlignmentBase;
import frc.robot.subsystems.shooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

public class PickUpCargo extends SequentialCommandGroup {

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

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }

}