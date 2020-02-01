/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.climber;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.SparkMaxController;

public class RealClimber extends ClimberBase {
  /**
   * Creates a new RealClimber.
   */
  public DoubleSolenoid4905 grapplingHook;
  public SparkMaxController winch;

  public RealClimber() {
    Config climberConf = Config4905.getConfig4905().getClimberConfig();
    grapplingHook = new DoubleSolenoid4905(climberConf, "grapplingHook");
    winch = new SparkMaxController(climberConf, "winch");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void extendGrapplingHook() {
    // TODO Auto-generated method stub
    grapplingHook.extendPiston();
  }

  @Override
  public void retractGrapplingHook() {
    // TODO Auto-generated method stub
    grapplingHook.retractPiston();
  }

  @Override
  public void driveWinch() {
    // TODO Auto-generated method stub
    winch.set(1);
  }
}
