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
import frc.robot.sensors.NavXGyroSensor;

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

    leftBrake = new DoubleSolenoid4905(climberConf, "leftBrake");
    rightBrake = new DoubleSolenoid4905(climberConf, "rightBrake");
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void driveLeftWinch() {
    // TODO Auto-generated method stub
    leftWinch.set(1);
  }

  @Override
  public void driveRightWinch() {
    // TODO Auto-generated method stub
    rightWinch.set(1);
  }

  @Override
  public void ascend() {
    // TODO Auto-generated method stub
    driveLeftWinch();
    driveRightWinch();
  }

  @Override
  public void stopWinch() {
    // TODO Auto-generated method stub
    leftWinch.set(0);
    rightWinch.set(0);
  }

  @Override
  public void adjustWinch(int adjust) {
    // TODO Auto-generated method stub
    leftWinch.set(adjust);
    rightWinch.set(adjust);
  }

  @Override
  public void extendArms() {
    // TODO Auto-generated method stub
    leftGrapplingHook.extendPiston();
    rightGrapplingHook.extendPiston();
  }

  @Override
  public void retractArms() {
    // TODO Auto-generated method stub
    leftGrapplingHook.retractPiston();
    rightGrapplingHook.retractPiston();
  }

  @Override
  public void stopArms() {
    // TODO Auto-generated method stub
    leftGrapplingHook.stopPiston();
    rightGrapplingHook.stopPiston();
  }

  public double getLeftWinchPosition() {
    return leftWinch.getEncoderPositionTicks();
  }

  public double getRightWinchPosition() {
    return rightWinch.getEncoderPositionTicks();
  }

  public double getLeftWinchVelocity() {
    return leftWinch.getEncoderVelocityTicks();
  }

  public double getRightWinchVelocity() {
    return rightWinch.getEncoderVelocityTicks();
  }

  @Override
  public double getBarAngle() {
    if (Math.abs(NavXGyroSensor.getInstance().getXAngle()) < 2) {
      double widthBetweenWinches = Config4905.getConfig4905().getClimberConfig().getDouble("widthBetweenWinches");
      double stringLengthDifference = Math.abs(getRightWinchPosition() - getLeftWinchPosition());
      double barAngle = Math.atan(stringLengthDifference / widthBetweenWinches);
      return barAngle;
    } else {
      return 0;
    }
  }
}
