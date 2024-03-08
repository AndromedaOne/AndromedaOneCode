// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;


/** Add your docs here. */
public class MockPhotonVision implements PhotonVisionBase {


  @Override
  public boolean doesTargetExist() {
    return false;
  }

  @Override
  public double getDistanceToTarget() {
    return 0;
  }

  @Override
  public double getTargetID() {
    return 0;
  }

}
