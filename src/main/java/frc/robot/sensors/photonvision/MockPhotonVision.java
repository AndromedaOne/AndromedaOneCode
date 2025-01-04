// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

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
  public boolean doesPhotonVisionExist() {
    return false;
  }

  @Override
  public double getDistanceToTargetInMeters(int wantedID) {
    return 0;
  }

  @Override
  public TargetDetectedAndAngle getTargetDetectedAndAngle(int wantedID, double setPoint) {
    return new TargetDetectedAndAngle(0, false);
  }

  @Override
  public TargetDetectedAndDistance getTargetDetectedAndDistance(int wantedID) {
    return new TargetDetectedAndDistance(0, false);
  }

  @Override
  public Rotation3d getRotation3d() {
    return new Rotation3d(0, 0, 0);
  }

  @Override
  public Translation3d getTranslation3d() {
    return new Translation3d(0, 0, 0);
  }

  @Override
  public List<AprilTagInfo> getAprilTagInfo() {
    return new ArrayList<AprilTagInfo>();
  }
}
