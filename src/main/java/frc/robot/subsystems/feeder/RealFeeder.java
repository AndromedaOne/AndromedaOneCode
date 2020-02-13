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

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Config4905;
import frc.robot.actuators.TalonSRXController;

public class RealFeeder extends FeederBase {
  /**
   * Creates a new RealFeeder.
   */
  public TalonSRXController m_stageOne;
  public TalonSRXController m_stageTwo;
  public TalonSRXController m_stageThree;
  private SpeedControllerGroup m_stageTwoAndThree;

  public RealFeeder() {
    Config feederConf = Config4905.getConfig4905().getFeederConfig();
    m_stageOne = new TalonSRXController(feederConf, "stageOne");
    m_stageTwo = new TalonSRXController(feederConf, "stageTwo");
    m_stageThree = new TalonSRXController(feederConf, "stageThree");
    m_stageTwoAndThree = new SpeedControllerGroup(m_stageTwo, m_stageThree);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void driveStageOne() {
    m_stageOne.set(ControlMode.PercentOutput, 1);
  }

  public void stopStageOne() {
    m_stageOne.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void driveStageTwo() {
    m_stageTwoAndThree.set(1);
  }

  public void stopStageTwo() {
    m_stageTwoAndThree.set(0);
  }

  @Override
  public void driveBothStages() {
    driveStageOne();
    driveStageTwo();
  }

  @Override
  public void stopBothStages() {
    stopStageOne();
    stopStageTwo();
  }

  @Override
  public void runReverseBothStages() {
    runReverseStageOne();
    runReverseStageTwo();
  }

  @Override
  public void runReverseStageOne() {
    m_stageOne.set(ControlMode.PercentOutput, -1);

  }

  @Override
  public void runReverseStageTwo() {
    m_stageTwoAndThree.set(-1);

  }
}
