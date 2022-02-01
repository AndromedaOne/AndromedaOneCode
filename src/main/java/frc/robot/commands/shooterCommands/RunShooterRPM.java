// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Config4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class RunShooterRPM extends CommandBase {

  private ShooterBase m_topShooterWheel;
  private ShooterBase m_bottomShooterWheel;
  private double m_setpoint = 0;

  public RunShooterRPM(ShooterBase topShooterWheel, ShooterBase bottomShooterWheel,
      double setpoint) {
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    m_setpoint = setpoint;
    addRequirements(topShooterWheel, bottomShooterWheel);

  }

  public RunShooterRPM(ShooterBase topShooterWheel, ShooterBase bottomShooterWheel) {
    this(topShooterWheel, bottomShooterWheel, SmartDashboard.getNumber("Run Shooter RPM", 1000));
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    CommandScheduler.getInstance().schedule(
        new RunOneShooterWheelVelocity(m_topShooterWheel, () -> m_setpoint,
            Config4905.getConfig4905().getShooterConfig()),
        new RunOneShooterWheelVelocity(m_bottomShooterWheel, () -> m_setpoint,
            Config4905.getConfig4905().getShooterConfig()));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
