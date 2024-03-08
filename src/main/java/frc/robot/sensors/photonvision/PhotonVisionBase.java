// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

/** Add your docs here. */
public interface PhotonVisionBase {

  public boolean doesTargetExist();

  public double getDistanceToTarget();

  public double getTargetID();

}