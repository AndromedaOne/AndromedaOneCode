/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.feeder;

import frc.robot.commands.DefaultFeederCommand;

public class MockFeeder extends FeederBase {
  /**
   * Creates a new MockFeeder.
   */
  public MockFeeder() {
    setDefaultCommand(new DefaultFeederCommand());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void driveStageOne() {
    // TODO Auto-generated method stub
    System.out.println("Driving feeder stage one");
  }

  @Override
  public void driveStageTwo() {
    // TODO Auto-generated method stub
    System.out.println("Driving feeder stage two");
  }

  @Override
  public void driveBothStages() {
    // TODO Auto-generated me hod stub
    System.out.println("Driving both feeder stages");
  }

  @Override
  public void stopStageOne() {
    // TODO Auto-generated method stub
    System.out.println("Stopping feeder stage one");
  }

  @Override
  public void stopStageTwo() {
    // TODO Auto-generated method stub
    System.out.println("Stopping feeder stage two");
  }

  @Override
  public void stopBothStages() {
    // TODO Auto-generated method stub
    System.out.println("Stopping both feeder stages");
  }
}
