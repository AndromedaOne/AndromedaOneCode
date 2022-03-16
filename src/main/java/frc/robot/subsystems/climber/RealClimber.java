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
    SmartDashboard.putBoolean("BackLeftWinchAtBottomLimitSwitch",
        backLeftWinchAtBottomLimitSwitch());
    SmartDashboard.putBoolean("BackRightWinchAtBottomLimitSwitch",
        backRightWinchAtBottomLimitSwitch());
    SmartDashboard.putNumber("ClimberHeightRightBack", getBackRightWinchAdjustedEncoderValue());
    SmartDashboard.putNumber("ClimberHeightLeftBack", getBackLeftWinchAdjustedEncoderValue());
  }

  @Override
  public void driveFrontLeftWinch() {

  }

  @Override
  public void driveFrontRightWinch() {

  }

  @Override
  public void driveBackRightWinch() {
    m_backRightWinch.set(1);

  }

  @Override
  public void stopFrontLeftWinch() {

  }

  @Override
  public void stopBackLeftWinch() {
    m_backLeftWinch.set(0);

  }

  @Override
  public void stopFrontRightWinch() {

  }

  @Override
  public void stopBackRightWinch() {
    m_backRightWinch.set(0);
  }

  @Override
  public void unwindFrontLeftWinch() {

  }

  @Override
  public void unwindBackLeftWinch() {
    m_backLeftWinch.set(-1);

  }

  @Override
  public void driveBackLeftWinch() {
    m_backLeftWinch.set(1);
  }

  @Override
  public void unwindFrontRightWinch() {

  }

  @Override
  public void unwindBackRightWinch() {
    m_backRightWinch.set(-1);

  }

  @Override
  public boolean backLeftWinchAtTopLimitSwitch() {
    return m_backLeftWinch.isReverseLimitSwitchOn();
  }

  @Override
  public boolean backLeftWinchAtBottomLimitSwitch() {
    return m_backLeftWinch.isForwardLimitSwitchOn();
  }

  @Override
  public boolean backRightWinchAtTopLimitSwitch() {
    return m_backRightWinch.isReverseLimitSwitchOn();
  }

  @Override
  public boolean backRightWinchAtBottomLimitSwitch() {
    return m_backRightWinch.isForwardLimitSwitchOn();
  }

  @Override
  public double getBackLeftWinchAdjustedEncoderValue() {
    return m_backLeftWinch.getEncoderPositionTicks() - m_initialEncoderBackLeftWinch;
  }

  @Override
  public double getBackRightWinchAdjustedEncoderValue() {
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