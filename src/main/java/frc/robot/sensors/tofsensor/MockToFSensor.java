// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.tofsensor;

/** Add your docs here. */
public class MockToFSensor implements ToFSensorBase {
  @Override
  public void updateSmartDashboard() {

  }

  @Override
  public double getDistance_mm() {
    return 0;
  }

  @Override
  public double getDistance_Inches() {
    return 0;
  }

  @Override
  public boolean isRangeValid() {
    return false;
  }
}
