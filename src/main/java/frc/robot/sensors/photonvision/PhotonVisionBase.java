// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

/** Add your docs here. */
public interface PhotonVisionBase {

  public boolean doesTargetExist(int wantedID);

  public double getDistanceToTargetInInches(int wantedID);

  public double getDistanceToTargetInInches(int wantedID);

  public double getTargetID();

  public DoubleSupplier getYaw(IntSupplier wantedID, DoubleSupplier setpoint);

  public boolean doesPhotonVisionExist();

}