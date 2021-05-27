// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.analog41IRSensor;

/** Add your docs here. */
public abstract class Analog41IRSensor {

  public abstract double getDistance();

  public abstract boolean isReal();

  public abstract void updateSmartDashboardReadings();

}
