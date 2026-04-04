// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intakerollers;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealIntakeRollers extends SubsystemBase implements IntakeRollersBase {
  private SparkMaxController m_leaderMotor;
  private SparkMaxController m_followerMotor;
  private Config m_intakeRollersConfig = Config4905.getConfig4905().getIntakeRollersConfig();

  // need to make a config for intake
  public RealIntakeRollers() {
    // we need to set the rollers
    // it is assumed pos is intake and neg is eject
    // all values are arbitrary
    m_leaderMotor = new SparkMaxController(m_intakeRollersConfig, "leaderMotor", false, false);
    m_followerMotor = new SparkMaxController(m_intakeRollersConfig, "leaderMotor", false, false);
  }

  @Override
  public void stop() {
    m_leaderMotor.setSpeed(0);
    m_followerMotor.setSpeed(0);
  }

  @Override
  public void intake() {
    m_leaderMotor.setSpeed(-1);
  }

  @Override
  public void eject() {
    m_leaderMotor.setSpeed(1);
  }

  @Override
  public SubsystemBase getSubsystemBase() {
    return this;
  }

  @Override
  public void setDefaultCommand(Command command) {
    super.setDefaultCommand(command);
  }

}
