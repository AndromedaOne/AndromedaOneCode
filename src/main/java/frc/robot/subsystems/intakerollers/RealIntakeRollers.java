// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intakerollers;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Config4905;
import frc.robot.actuators.TalonFXController;

/** Add your docs here. */
public class RealIntakeRollers extends SubsystemBase implements IntakeRollersBase {
  private TalonFXController m_leaderMotor;
  private TalonFXController m_followerMotor;
  private double m_speed = 1;
  private Config m_intakeRollersConfig = Config4905.getConfig4905().getIntakeRollersConfig();

  // need to make a config for intake
  public RealIntakeRollers() {
    // we need to set the rollers
    // it is assumed pos is intake and neg is eject
    // all values are arbitrary
    m_leaderMotor = new TalonFXController(m_intakeRollersConfig, "leaderMotor");
    m_followerMotor = new TalonFXController(m_intakeRollersConfig, "followerMotor");
  }

  @Override
  public void stop() {
    m_leaderMotor.setSpeed(0);
    m_followerMotor.setSpeed(0);
  }

  @Override
  public void intake() {
    m_leaderMotor.setSpeed(m_speed);
    m_followerMotor.setSpeed(m_speed);
  }

  @Override
  public void eject() {
    m_leaderMotor.setSpeed(-m_speed);
    m_followerMotor.setSpeed(-m_speed);
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
