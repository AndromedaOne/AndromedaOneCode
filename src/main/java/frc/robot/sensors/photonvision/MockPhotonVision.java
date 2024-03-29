// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import frc.robot.sensors.photonvision.RealPhotonVision;
import frc.robot.sensors.photonvision.RealPhotonVision.PhotonVisionYawSupplier;

/** Add your docs here. */
public class MockPhotonVision implements PhotonVisionBase {

  @Override
  public boolean doesTargetExist(int wantedID) {
    return false;
  }

  @Override
  public double getDistanceToTargetInInches(int wantedID) {
    return 0;
  }

  @Override
  public double getTargetID() {
    return 0;
  }

  @Override
  public DoubleSupplier getYaw(IntSupplier wantedID, DoubleSupplier setpoint) {
    return () -> 0;
  }

  @Override
  public boolean doesPhotonVisionExist() {
    return false;
  }

  @Override
  public double getDistanceToTargetInMeters(int wantedID) {
    return 0;
  }

  @Override
  public PhotonVisionYawSupplier getPhotonVisionSupplier(IntSupplier wantedID, DoubleSupplier setpoint) {
    RealPhotonVision = new RealPhotonVision();
    return(new PhotonVisionYawSupplier(() -> -1, () -> 0));
  }
}
