// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooterCommands;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.subsystems.shooter.ShooterBase;
import frc.robot.telemetries.Trace;

public class RunShooterRPM extends ParallelCommandGroup {

  private ShooterBase m_topShooterWheel;
  private ShooterBase m_bottomShooterWheel;
  private double m_setpoint = 0;
  private boolean m_useSmartDashboardRPM = false;
  private BooleanSupplier m_finishedCondition;
  private boolean m_finished = false;

  public RunShooterRPM(ShooterBase topShooterWheel, ShooterBase bottomShooterWheel, double setpoint,
      boolean useSmartDashboardRPM, BooleanSupplier finishedCondition) {
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    m_setpoint = setpoint;
    m_useSmartDashboardRPM = useSmartDashboardRPM;
    m_finishedCondition = finishedCondition;
    if (useSmartDashboardRPM) {
      m_finishedCondition = new FinishedConditionSupplier();
    }
    addCommands(
        new RunOneShooterWheelVelocity(m_topShooterWheel, () -> m_setpoint,
            Config4905.getConfig4905().getShooterConfig(), m_finishedCondition),
        new RunOneShooterWheelVelocity(m_bottomShooterWheel, () -> m_setpoint,
            Config4905.getConfig4905().getShooterConfig(), m_finishedCondition));

  }

  public RunShooterRPM(ShooterBase topShooterWheel, ShooterBase bottomShooterWheel) {
    this(topShooterWheel, bottomShooterWheel, 0, true, () -> false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    if (m_useSmartDashboardRPM) {
      m_setpoint = SmartDashboard.getNumber("Set Shooter RPM", 1000);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_topShooterWheel.setShooterWheelPower(0);
    m_bottomShooterWheel.setShooterWheelPower(0);
    m_finished = true;
    Trace.getInstance().logCommandStop(this);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_finishedCondition.getAsBoolean();
  }

  private class FinishedConditionSupplier implements BooleanSupplier {

    @Override
    public boolean getAsBoolean() {
      return m_finished;
    }

  }
}
