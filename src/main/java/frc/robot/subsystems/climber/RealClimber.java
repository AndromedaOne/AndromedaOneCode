package frc.robot.subsystems.climber;

import com.revrobotics.CANSparkMax.IdleMode;
import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealClimber extends ClimberBase {
  public SparkMaxController m_backLeftWinch;
  public SparkMaxController m_backRightWinch;
  private double m_initialEncoderBackRightWinch = 0;
  private double m_initialEncoderBackLeftWinch = 0;

  public RealClimber() {
    Config climberConfig = Config4905.getConfig4905().getClimberConfig();
    m_backLeftWinch = new SparkMaxController(climberConfig, "backLeftWinch");
    m_backRightWinch = new SparkMaxController(climberConfig, "backRightWinch");
    m_initialEncoderBackLeftWinch = m_backLeftWinch.getEncoderPositionTicks();
    m_initialEncoderBackRightWinch = m_backRightWinch.getEncoderPositionTicks();
  }

  public void periodic() {
    SmartDashboard.putBoolean("BackLeftWinchAtBottomLimitSwitch", leftWinchAtBottomLimitSwitch());
    SmartDashboard.putBoolean("BackRightWinchAtBottomLimitSwitch", rightWinchAtBottomLimitSwitch());
    SmartDashboard.putNumber("ClimberHeightRightBack", getRightWinchAdjustedEncoderValue());
    SmartDashboard.putNumber("ClimberHeightLeftBack", getLeftWinchAdjustedEncoderValue());
  }

  @Override
  public void driveRightWinch(double speed) {
    m_backRightWinch.set(speed);

  }

  @Override
  public void stopLeftWinch() {
    m_backLeftWinch.set(0);

  }

  @Override
  public void stopRightWinch() {
    m_backRightWinch.set(0);
  }

  @Override
  public void unwindLeftWinch() {
    m_backLeftWinch.set(-1);

  }

  @Override
  public void driveLeftWinch(double speed) {
    m_backLeftWinch.set(speed);
  }

  @Override
  public void unwindRightWinch() {
    m_backRightWinch.set(-1);

  }

  @Override
  public boolean leftWinchAtTopLimitSwitch() {
    return m_backLeftWinch.isReverseLimitSwitchOn();
  }

  @Override
  public boolean leftWinchAtBottomLimitSwitch() {
    return m_backLeftWinch.isForwardLimitSwitchOn();
  }

  @Override
  public boolean rightWinchAtTopLimitSwitch() {
    return m_backRightWinch.isReverseLimitSwitchOn();
  }

  @Override
  public boolean rightWinchAtBottomLimitSwitch() {
    return m_backRightWinch.isForwardLimitSwitchOn();
  }

  @Override
  public double getLeftWinchAdjustedEncoderValue() {
    return m_backLeftWinch.getEncoderPositionTicks() - m_initialEncoderBackLeftWinch;
  }

  @Override
  public double getRightWinchAdjustedEncoderValue() {
    return m_backRightWinch.getEncoderPositionTicks() - m_initialEncoderBackRightWinch;
  }

  @Override
  public void setWinchBrakeMode(boolean brakeOn) {
    if (brakeOn) {
      m_backLeftWinch.setIdleMode(IdleMode.kBrake);
      m_backRightWinch.setIdleMode(IdleMode.kBrake);
    } else {
      m_backLeftWinch.setIdleMode(IdleMode.kCoast);
      m_backRightWinch.setIdleMode(IdleMode.kCoast);
    }
  }
}