// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.encoder;

/** Add your docs here. */
public abstract class EncoderBase {

  public abstract void resetEncoder();

  public abstract double getEncoderValue();

  public abstract void updateSmartDashboardReadings();
}
