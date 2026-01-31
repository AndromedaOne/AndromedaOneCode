// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.hopperbelts;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealHopperBelts extends SubsystemBase implements HopperBeltsBase {
  private SparkMaxController m_frontBelts;
  private SparkMaxController m_backBelts;
  private Config m_beltConfig = Config4905.getConfig4905().getHopperBeltsConfig();

  public RealHopperBelts() {
    // this is all assuming:
    // front belts - pos is towards middle, neg is away from middle
    // back belts - pos is away from middle, neg is towards middle
    // all speeds are arbitrary
    m_frontBelts = new SparkMaxController(m_beltConfig, "hopperFrontBeltsMotor", false, false);
    m_backBelts = new SparkMaxController(m_beltConfig, "hopperBackBeltsMotor", false, false);
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

  @Override
  public void stop() {
    m_frontBelts.setSpeed(0);
    m_backBelts.setSpeed(0);
  }

  @Override
  public void intake() {
    m_frontBelts.setSpeed(0.5);
    // we dont know what we want the back belts doing.
    // what they do will be the result of later experimentation.
    m_backBelts.setSpeed(0);
  }

  @Override
  public void eject() {
    m_frontBelts.setSpeed(0.7);
    m_backBelts.setSpeed(-0.7);
  }

  @Override
  public void ejectThroughIntake() {
    m_frontBelts.setSpeed(-0.7);
    m_backBelts.setSpeed(-0.7);
  }

  @Override
  public void setBrakeMode() {
    m_frontBelts.setBrakeMode();
    m_backBelts.setBrakeMode();
  }

  @Override
  public void setCoastMode() {
    m_frontBelts.setCoastMode();
    m_backBelts.setCoastMode();
  }

}
