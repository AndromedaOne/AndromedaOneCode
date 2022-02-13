// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.groupCommands.shooterFeederCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.feeder.RealFeeder;
import frc.robot.subsystems.shooter.ShooterWheelBase;

public class StopShooterFeeder extends CommandBase {
  private RealFeeder m_realFeeder;
  private ShooterWheelBase m_topShooterWheel;
  private ShooterWheelBase m_bottomShooterWheel;
  private BooleanSupplier m_finishedCondition;

  /** Creates a new StopShooterFeeder. */
  public StopShooterFeeder(RealFeeder realFeeder, ShooterWheelBase topShooterWheel,
      ShooterWheelBase bottomShooterWheel, BooleanSupplier finishedCondition) {
    m_realFeeder = realFeeder;
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    m_finishedCondition = finishedCondition;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_realFeeder.stopFeeder();
    m_topShooterWheel.setShooterWheelPower(0);
    m_bottomShooterWheel.setShooterWheelPower(0);
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
    return true;
  }
}
