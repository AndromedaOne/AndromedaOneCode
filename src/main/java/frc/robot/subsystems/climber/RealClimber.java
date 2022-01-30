package frc.robot.subsystems.climber;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealClimber extends ClimberBase {
  public SparkMaxController m_backLeftWinch;
  public SparkMaxController m_backRightWinch;
  public SparkMaxController m_frontLeftWinch;
  public SparkMaxController m_frontRightWinch;

  public RealClimber() {
    Config climberConfig = Config4905.getConfig4905().getClimberConfig();

  }

  @Override
  public void extendFrontLeftArm() {

  }

  @Override
  public void extendBackLeftArm() {

  }

  @Override
  public void extendFrontRightArm() {

  }

  @Override
  public void extendBackRightArm() {

  }

  @Override
  public void retractFrontLeftArm() {

  }

  @Override
  public void retractBackLeftArm() {

  }

  @Override
  public void retractFrontRightArm() {

  }

  @Override
  public void retractBackRightArm() {

  }

  @Override
  public void driveFrontLeftWinch() {

  }

  @Override
  public void driveBackLeftWinch() {
    m_backLeftWinch.set(1.0);
  }

  @Override
  public void driveFrontRightWinch() {

  }

  @Override
  public void driveBacktRightWinch() {
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

}