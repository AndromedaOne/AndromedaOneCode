// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.function.DoubleSupplier;

/** Add your docs here. */
public interface PhotonVisionBase {
  public DoubleSupplier getYawDoubleSupplier();

  public boolean doesTargetExist();

  public double getDistanceToTarget();

}