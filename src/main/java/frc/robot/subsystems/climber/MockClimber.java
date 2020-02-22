/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.climber;

import frc.robot.actuators.SparkMaxController;

public class MockClimber extends ClimberBase {
  /**
   * Creates a new MockClimber.
   */
  public MockClimber() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void driveLeftWinch() {

  }

  @Override
  public void driveRightWinch() {

  }

  @Override
  public void ascend() {

  }

  @Override
  public void stopLeftWinch() {

  }

  @Override
  public void extendArms() {
  }

  @Override
  public void retractArms() {
  }

  @Override
  public void stopArms() {
  }

  @Override
  public void stopRightWinch() {

  }

  @Override
  public SparkMaxController getLeftWinch() {
    return null;
  }

  @Override
  public SparkMaxController getRightWinch() {
    return null;
  }

  @Override
  public void adjustLeftWinch(double adjust) {

  }

  @Override
  public void adjustRightWinch(double adjust) {

  }
}
