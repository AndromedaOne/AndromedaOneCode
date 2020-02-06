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
    // This method will be called once per scheduler run
  }

  @Override
  public void driveLeftWinch() {
    // TODO Auto-generated method stub

  }

  @Override
  public void driveRightWinch() {
    // TODO Auto-generated method stub

  }

  @Override
  public void ascend() {
    // TODO Auto-generated method stub

  }

  @Override
  public void stopWinch() {
    // TODO Auto-generated method stub

  }

  @Override
  public void adjustWinch(int adjust) {
    // TODO Auto-generated method stub

  }

  @Override
  public void extendArms() {
    // TODO Auto-generated method stub
     System.out.println("Extending arms");
  }

  @Override
  public void retractArms() {
    // TODO Auto-generated method stub
     System.out.println("Retracting arms");
  }

  @Override
  public void stopArms() {
    // TODO Auto-generated method stub
   System.out.println("Stopping arms");
  }
}
