// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.List;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

/** Add your docs here. */
public interface PhotonVisionBase {

  public boolean doesTargetExist(int wantedID);

  public double getDistanceToTargetInInches(int wantedID);

  public double getDistanceToTargetInMeters(int wantedID);

  public double getTargetID();

  public boolean doesPhotonVisionExist();

  public TargetDetectedAndAngle getTargetDetectedAndAngle(int wantedID, double setPoint);

  public TargetDetectedAndDistance getTargetDetectedAndDistance(int wantedID);

  public Rotation3d getRotation3d();

  public Translation3d getTranslation3d();

  public class AprilTagInfo {
    public int aprilTagID;
    public double distanceToTarget;
    public double angleToTarget;
    public double ambiguity;
  }

  public List<AprilTagInfo> getAprilTagInfo();

  public PhotonCamera getPhotonCamera();

  public void computeDistanceAndAngle(int wantedID, boolean useTrace, boolean useLeft,
      TargetDistanceAndAngle targetDistanceAngle);

}