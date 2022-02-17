// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.subsystems.shooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

public class RunShooter extends ParallelCommandGroup {
  private ShooterWheelBase m_topShooterWheel;
  private ShooterWheelBase m_bottomShooterWheel;
  private double m_setpoint = 0;
  private boolean m_runInReverse = false;

  /** Creates a new RunShooter. */
  public RunShooter(ShooterWheelBase topShooterWheel, ShooterWheelBase bottomShooterWheel,
      double setpoint, boolean runInReverse) {
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    m_setpoint = setpoint;
    m_runInReverse = runInReverse;
    addCommands(
        new RunOneShooterWheelVelocity(m_topShooterWheel, () -> m_setpoint,
            Config4905.getConfig4905().getShooterConfig(), () -> false, m_runInReverse),
        new RunOneShooterWheelVelocity(m_bottomShooterWheel, () -> m_setpoint,
            Config4905.getConfig4905().getShooterConfig(), () -> false, m_runInReverse));
  }

  public RunShooter(ShooterWheelBase topShooterWheel, ShooterWheelBase bottomShooterWheel) {
    this(topShooterWheel, bottomShooterWheel, 0, false);
  }

  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    System.out.println("Setpoint Set To" + m_setpoint);
    super.initialize();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
