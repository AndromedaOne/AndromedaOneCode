// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.feeder.RealFeeder;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class RunShooterFeeder extends ParallelCommandGroup {
  private RealFeeder m_realFeeder;
  private double m_speed = 0;
  private BooleanSupplier m_finishedCondition;
  private ShooterWheelBase m_topShooterWheel;
  private ShooterWheelBase m_bottomShooterWheel;

  public RunShooterFeeder(RealFeeder realFeeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, BooleanSupplier finishedCondition) {
    m_realFeeder = realFeeder;
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    m_finishedCondition = finishedCondition;
    // addRequirements()
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_finishedCondition.getAsBoolean();
  }
}
