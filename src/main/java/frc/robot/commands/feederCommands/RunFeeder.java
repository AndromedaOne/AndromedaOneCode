// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.feederCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.feeder.FeederBase;
import frc.robot.telemetries.Trace;

public class RunFeeder extends CommandBase {
  private FeederBase m_feeder;
  private DoubleSupplier m_speed;
  private boolean m_runInReverse;
  private BooleanSupplier m_readyToShoot;

  /** Creates a new RunFeeder. */
  public RunFeeder(FeederBase feeder, DoubleSupplier speed, boolean runInReverse,
      BooleanSupplier readyToShoot) {
    m_feeder = feeder;
    m_speed = speed;
    m_runInReverse = runInReverse;
    m_readyToShoot = readyToShoot;
    addRequirements(m_feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (Robot.getInstance().getOIContainer().getSubsystemController()
        .getPauseFeederButtonPressed()) {
      m_feeder.runFeeder(0);
    } else if (m_runInReverse) {
      m_feeder.runFeeder(-m_speed.getAsDouble());
    } else if (m_readyToShoot.getAsBoolean()) {
      m_feeder.runFeeder(m_speed.getAsDouble());
    // } else if (!m_runInReverse && !m_readyToShoot.getAsBoolean()) {
    //  m_feeder.runFeeder(m_speed.getAsDouble());
    } else {
      m_feeder.runFeeder(0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    m_feeder.stopFeeder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
