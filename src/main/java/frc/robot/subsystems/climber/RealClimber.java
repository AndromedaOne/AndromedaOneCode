/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.climber;

import com.typesafe.config.Config;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.actuators.DoubleSolenoid4905;
import frc.robot.actuators.SparkMaxController;
import frc.robot.oi.SmartDashboard4905;

public class RealClimber extends ClimberBase {
  /**
   * Creates a new RealClimber.
   */
  public DoubleSolenoid4905 leftGrapplingHook;
  public DoubleSolenoid4905 rightGrapplingHook;
  public SparkMaxController leftWinch;
  public SparkMaxController rightWinch;
  public DoubleSolenoid4905 leftBrake;
  public DoubleSolenoid4905 rightBrake;


  public RealClimber() {
    Config climberConf = Config4905.getConfig4905().getClimberConfig();
    leftGrapplingHook = new DoubleSolenoid4905(climberConf, "leftGrapplingHook");
    leftWinch = new SparkMaxController(climberConf, "leftWinch");
    rightGrapplingHook = new DoubleSolenoid4905(climberConf, "rightGrapplingHook");
    rightWinch = new SparkMaxController(climberConf, "rightWinch");

    // leftBrake = new DoubleSolenoid4905(climberConf, "leftBrake");
    // rightBrake = new DoubleSolenoid4905(climberConf, "rightBrake");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (!isTimeToClimb()) {
      stopLeftWinch();
      stopRightWinch();
      retractArms();
    }
  }

  @Override
  public void driveLeftWinch() {
    leftWinch.set(1);
  }

  @Override
  public void driveRightWinch() {
    rightWinch.set(1);
  }

  @Override
  public void ascend() {
    driveLeftWinch();
    driveRightWinch();
  }

  @Override
  public void stopLeftWinch() {
    leftWinch.set(0);
  }

  @Override
  public void stopRightWinch() {
    rightWinch.set(0);
  }

  @Override
  public void adjustWinch(int adjust) {
    leftWinch.set(adjust);
    rightWinch.set(adjust);
  }

  @Override
  public void extendArms() {
    leftGrapplingHook.extendPiston();
    rightGrapplingHook.extendPiston();
  }

  @Override
  public void retractArms() {
    leftGrapplingHook.retractPiston();
    rightGrapplingHook.retractPiston();
  }

  @Override
  public void stopArms() {
    leftGrapplingHook.stopPiston();
    rightGrapplingHook.stopPiston();
  }

  @Override
  public boolean isTimeToClimb() {
    double matchTime = DriverStation.getInstance().getMatchTime();
    if (matchTime >= 45) {
      return true;
    } else {
      return false;
    }
  }
}
