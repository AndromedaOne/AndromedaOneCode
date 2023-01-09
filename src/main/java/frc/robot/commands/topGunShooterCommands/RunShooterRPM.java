// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.topGunShooterCommands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Config4905;
import frc.robot.subsystems.topGunShooter.ShooterWheelBase;
import frc.robot.telemetries.Trace;

public class RunShooterRPM extends ParallelCommandGroup {

  private ShooterWheelBase m_topShooterWheel;
  private ShooterWheelBase m_bottomShooterWheel;
  private DoubleSupplier m_setpoint;
  private boolean m_useSmartDashboardRPM = false;
  private BooleanSupplier m_finishedCondition;
  private boolean m_finished = false;
  private RunOneShooterWheelVelocity m_topShooterCommand;
  private RunOneShooterWheelVelocity m_bottomShooterCommand;

  public RunShooterRPM(ShooterWheelBase topShooterWheel, ShooterWheelBase bottomShooterWheel,
      DoubleSupplier setpoint, boolean useSmartDashboardRPM, BooleanSupplier finishedCondition) {
    m_topShooterWheel = topShooterWheel;
    m_bottomShooterWheel = bottomShooterWheel;
    m_setpoint = setpoint;
    m_useSmartDashboardRPM = useSmartDashboardRPM;
    m_finishedCondition = finishedCondition;

    m_topShooterCommand = new RunOneShooterWheelVelocity(m_topShooterWheel, m_setpoint,
        Config4905.getConfig4905().getShooterConfig(), m_finishedCondition);
    m_bottomShooterCommand = new RunOneShooterWheelVelocity(m_bottomShooterWheel, m_setpoint,
        Config4905.getConfig4905().getShooterConfig(), m_finishedCondition);

    if (useSmartDashboardRPM) {
      m_finishedCondition = new FinishedConditionSupplier();
    }
    addCommands(m_topShooterCommand, m_bottomShooterCommand);
  }

  public RunShooterRPM(ShooterWheelBase topShooterWheel, ShooterWheelBase bottomShooterWheel,
      DoubleSupplier setpoint) {
    this(topShooterWheel, bottomShooterWheel, setpoint, false, () -> false);
  }

  public RunShooterRPM(ShooterWheelBase topShooterWheel, ShooterWheelBase bottomShooterWheel) {
    this(topShooterWheel, bottomShooterWheel, () -> 0, true, () -> false);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Trace.getInstance().logCommandStart(this);
    if (m_useSmartDashboardRPM) {
      m_setpoint = () -> SmartDashboard.getNumber("Set Shooter RPM", 1000);
      m_finished = false;
    }
    System.out.println("Setpoint Set To" + m_setpoint);
    super.initialize();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_topShooterWheel.setShooterWheelPower(0);
    m_bottomShooterWheel.setShooterWheelPower(0);
    m_finished = true;
    Trace.getInstance().logCommandStop(this);
    super.end(interrupted);
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

  public BooleanSupplier atSetpoint() {
    if (m_topShooterCommand.atSetpoint() && m_bottomShooterCommand.atSetpoint()) {
      System.out.println(
          "Shooter Wheel At Setpoint WheelSpeed: " + m_topShooterWheel.getShooterWheelRpm());
    }
    return () -> (m_topShooterCommand.atSetpoint() && m_bottomShooterCommand.atSetpoint());
  }
}
