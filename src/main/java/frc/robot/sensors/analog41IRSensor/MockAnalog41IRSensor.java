// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.analog41IRSensor;

/** Add your docs here. */
public class MockAnalog41IRSensor implements Analog41IRSensor {

  @Override
  public double getDistance() {
    return 0;
  }

  @Override
  public boolean isReal() {
    return false;
  }

}
