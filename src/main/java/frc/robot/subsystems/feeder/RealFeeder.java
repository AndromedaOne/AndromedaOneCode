/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.feeder;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.commands.DefaultFeederCommand;

public class RealFeeder extends FeederBase {
  /**
   * Creates a new RealFeeder.
   */
  public TalonSRX m_stageOne;
  public TalonSRX m_stageTwo;

  public RealFeeder() {
    Config feederConf = Config4905.getConfig4905().getFeederConfig();
    m_stageOne = new TalonSRX(feederConf.getInt("ports.stageOne"));
    m_stageTwo = new TalonSRX(feederConf.getInt("ports.stageTwo"));

    setDefaultCommand(new DefaultFeederCommand());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void driveStageOne() {
    // TODO Auto-generated method stub
    m_stageOne.set(ControlMode.PercentOutput, 1);
  }

  public void stopStageOne() {
    m_stageOne.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void driveStageTwo() {
    // TODO Auto-generated method stub
    m_stageTwo.set(ControlMode.PercentOutput, 1);
  }

  public void stopStageTwo() {
    m_stageTwo.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void driveBothStages() {
    // TODO Auto-generated method stub
    driveStageOne();
    driveStageTwo();
  }

  public void stopBothStages() {
    stopStageOne();
    stopStageTwo();
  }
}
