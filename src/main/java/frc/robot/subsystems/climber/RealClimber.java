package frc.robot.subsystems.climber;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealClimber extends ClimberBase {
  public SparkMaxController m_backLeftWinch;
  public SparkMaxController m_backRightWinch;

  public RealClimber() {
    Config climberConfig = Config4905.getConfig4905().getClimberConfig();
    m_backLeftWinch = new SparkMaxController(climberConfig, "backLeftWinch");
    m_backRightWinch = new SparkMaxController(climberConfig, "backRightWinch");
  }

  public void periodic() {
    SmartDashboard.putBoolean("BackLeftWinchAtTopLimitSwitch", backLeftWinchAtTopLimitSwitch());
    SmartDashboard.putBoolean("BackLeftWinchAtBottomLimitSwitch",
        backLeftWinchAtBottomLimitSwitch());
    SmartDashboard.putBoolean("BackRightWinchAtTopLimitSwitch", backRightWinchAtTopLimitSwitch());
    SmartDashboard.putBoolean("BackRightWinchAtBottomLimitSwitch",
        backRightWinchAtBottomLimitSwitch());
  }

  @Override
  public void driveFrontLeftWinch() {

  }

  @Override
  public void driveFrontRightWinch() {

  }

  @Override
  public void driveBackRightWinch() {
    m_backRightWinch.set(1.0);

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
  public void stopBacktRightWinch() {
    m_backRightWinch.set(0);
  }

  @Override
  public SparkMaxController getBackLeftWinch() {
    return (m_backLeftWinch);
  }

  @Override
  public SparkMaxController getBackRightWinch() {
    return (m_backRightWinch);
  }

  @Override
  public void unwindFrontLeftWinch() {

  }

  @Override
  public void unwindBackLeftWinch() {
    m_backLeftWinch.set(-1.0);

  }

  @Override
  public void driveBackLeftWinch() {
    m_backLeftWinch.set(1.0);
  }

  @Override
  public void unwindFrontRightWinch() {

  }

  @Override
  public void unwindBackRightWinch() {
    m_backRightWinch.set(-1.0);

  }

  @Override
  public boolean backLeftWinchAtTopLimitSwitch() {
    return m_backLeftWinch.isForwardLimitSwitchOn();
  }

  @Override
  public boolean backLeftWinchAtBottomLimitSwitch() {
    return m_backLeftWinch.isReverseLimitSwitchOn();
  }

  @Override
  public boolean backRightWinchAtTopLimitSwitch() {
    return m_backRightWinch.isForwardLimitSwitchOn();
  }

  @Override
  public boolean backRightWinchAtBottomLimitSwitch() {
    return m_backRightWinch.isReverseLimitSwitchOn();
  }

}