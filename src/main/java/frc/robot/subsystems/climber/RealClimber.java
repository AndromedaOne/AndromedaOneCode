package frc.robot.subsystems.climber;

import com.typesafe.config.Config;

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

}