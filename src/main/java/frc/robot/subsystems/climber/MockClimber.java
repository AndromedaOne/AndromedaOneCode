/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.climber;

public class MockClimber extends ClimberBase {
  /**
   * Creates a new MockClimber.
   */
  public MockClimber() {

  }

  @Override
  public void periodic() {
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
  public void adjustWinch(int adjust) {
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
  public boolean isTimeToClimb() {
    return true;
  }
}
