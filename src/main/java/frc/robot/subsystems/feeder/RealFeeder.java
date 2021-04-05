/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.feeder;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.actuators.TalonSRXController;

public class RealFeeder extends FeederBase {
  /**
   * Creates a new RealFeeder.
   */
  public TalonSRXController m_stageOne;
  public TalonSRXController m_stageTwo;
  public SparkMaxController m_stageThree;

  public RealFeeder() {
    Config feederConf = Config4905.getConfig4905().getFeederConfig();
    m_stageOne = new TalonSRXController(feederConf, "stageOne");
    m_stageTwo = new TalonSRXController(feederConf, "stageTwo");
    m_stageThree = new SparkMaxController(feederConf, "stageThree");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void runStageOne(double speed) {
    m_stageOne.set(ControlMode.PercentOutput, speed);
  }

  public void stopStageOne() {
    m_stageOne.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void runStagesTwoAndThree(double stageTwoSpeed, double stageThreeSpeed) {
    m_stageTwo.set(ControlMode.PercentOutput, stageTwoSpeed);
    m_stageThree.set(stageThreeSpeed);
  }

  public void stopStageTwo() {
    m_stageTwo.set(0);
    m_stageThree.set(0);
  }

  @Override
  public void runBothStages(double stageOneAndTwoSpeed, double stageThreeSpeed) {
    runStageOne(stageOneAndTwoSpeed);
    runStagesTwoAndThree(stageOneAndTwoSpeed, stageThreeSpeed);
  }

  @Override
  public void stopBothStages() {
    stopStageOne();
    stopStageTwo();
  }

  @Override
  public void runReverseBothStages(double stageOneAndTwoSpeed, double stageThreeSpeed) {
    runReverseStageOne(stageOneAndTwoSpeed);
    runReverseStageTwo(stageOneAndTwoSpeed, stageThreeSpeed);
  }

  @Override
  public void runReverseStageOne(double speed) {
    m_stageOne.set(ControlMode.PercentOutput, -speed);

  }

  @Override
  public void runReverseStageTwo(double stageTwoSpeed, double stageThreeSpeed) {
    m_stageTwo.set(ControlMode.PercentOutput, -stageTwoSpeed);
    m_stageThree.set(-stageThreeSpeed);

  }

  @Override
  public void runStageThree(double speed) {

  }
}
