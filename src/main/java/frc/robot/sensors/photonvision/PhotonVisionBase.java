// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.List;

/** Add your docs here. */
public interface PhotonVisionBase {

  public boolean doesTargetExist(int wantedID);

  public double getDistanceToTargetInInches(int wantedID);

  public double getDistanceToTargetInMeters(int wantedID);

  public double getTargetID();

  public boolean doesPhotonVisionExist();

  public TargetDetectedAndAngle getTargetDetectedAndAngle(int wantedID, double setPoint);

  public TargetDetectedAndDistance getTargetDetectedAndDistance(int wantedID);

  public class AprilTagInfo {
    int aprilTagID;
    double distanceToTarget;
    double angleToTarget;
    double ambiguity;
  }

  public List<AprilTagInfo> getAprilTagInfo();

}