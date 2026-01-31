// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intakerollers;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.actuators.SparkMaxController;

/** Add your docs here. */
public class RealIntakeRollers extends SubsystemBase implements IntakeRollersBase {
  private SparkMaxController m_roller;

  // need to make a config for intake
  public RealIntakeRollers() {
    // we need to set the rollers
    // it is assumed pos is intake and neg is eject
    // all values are arbitrary
  }

  @Override
  public void stop() {
    m_roller.setSpeed(0);
  }

  @Override
  public void intake() {
    m_roller.setSpeed(0.5);
  }

  @Override
  public void eject() {
    m_roller.setSpeed(-0.5);
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
