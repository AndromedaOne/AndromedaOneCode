/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.topGunFeeder;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

public class RealFeeder extends FeederBase {
  private Config m_feederConfig = Config4905.getConfig4905().getFeederConfig();
  private SparkMaxController m_feederMotor;

  public RealFeeder() {
    m_feederMotor = new SparkMaxController(m_feederConfig, "feederMotor");
  }

  @Override
  public void runFeeder(double speed) {
    m_feederMotor.set(speed);
  }

  @Override
  public void stopFeeder() {
    m_feederMotor.set(0);
  }

  @Override
  public void runFeederInReverse(double speed) {
    m_feederMotor.set(-speed);

  }

}