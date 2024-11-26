// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.ArrayList;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.typesafe.config.Config;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

/** Add your docs here. */
public class RealPhotonVision extends RealSensorBase implements PhotonVisionBase {
  private PhotonCamera m_camera;
  private Config m_config = Config4905.getConfig4905().getSensorConfig();
  private double m_offsetToSwerveModInches = 0;
  private double m_cameraHeightInInches = 0;
  private double m_cameraHeightInMeters = 0;
  private double m_targetHeightInInches = 0;
  private double m_targetHeightInMeters = 0;
  private double m_cameraPitchInDegrees = 0;
  private double m_cameraPitchInRadians = 0;
  private double m_offset = 0;

  public class AprilTagInfo {
    int aprilTagID;
    double distanceToTarget;
    double angleToTarget;
    double ambiguity;
  }

  public RealPhotonVision(String cameraName) {
    // pass the name of the camera in
    // pass in photonvision.name
    m_camera = new PhotonCamera(cameraName);
    m_offsetToSwerveModInches = m_config
        .getDouble("photonvision." + cameraName + ".offsetToSwerveModInches");
    m_cameraHeightInInches = m_config
        .getDouble("photonvision." + cameraName + ".cameraHeightInInches");
    m_cameraHeightInMeters = m_cameraHeightInInches * 0.0254;
    m_targetHeightInInches = m_config
        .getDouble("photonvision." + cameraName + ".targetHeightInInches");
    m_targetHeightInMeters = m_targetHeightInInches * 0.0254;
    m_cameraPitchInDegrees = m_config
        .getDouble("photonvision." + cameraName + ".cameraPitchInDegrees");
    m_cameraPitchInRadians = Units.degreesToRadians(m_cameraPitchInDegrees);
    m_offset = m_config.getDouble("photonvision." + cameraName + ".cameraOffsetToCenterInInches");
    SmartDashboard.putBoolean("lost target", false);
  }

  public List<AprilTagInfo> getAprilTagInfo() {
    ArrayList<AprilTagInfo> info = new ArrayList<>();
    AprilTagInfo localInfo = new AprilTagInfo();

    for (PhotonTrackedTarget target : m_camera.getLatestResult().getTargets()) {
      localInfo.aprilTagID = target.getFiducialId();
      localInfo.distanceToTarget = getDistanceToTargetInMeters(localInfo.aprilTagID);
      localInfo.angleToTarget = getTargetAngle(target);
      localInfo.ambiguity = getAmbiguity(target);
      info.add(localInfo);
    }

    return info;
  }

  public double getAmbiguity(PhotonTrackedTarget target) {
    return target.getPoseAmbiguity();
  }

  @Override
  protected void updateSmartDashboard() {
    ArrayList<Double> iDArrayList = new ArrayList<>();

    for (PhotonTrackedTarget target : m_camera.getLatestResult().getTargets()) {
      iDArrayList.add((double) target.getFiducialId());
    }
    Double[] iDArray = new Double[iDArrayList.size()];
    iDArray = iDArrayList.toArray(iDArray);
    SmartDashboard.putNumberArray("Target IDs", iDArray);
    int tagID = (int) SmartDashboard.getNumber("PhotonVisionTagID For Distance", 4);
    SmartDashboard.putNumber("Photon Vision Range", getDistanceToTargetInInches(tagID));
  }

  @Override
  public boolean doesTargetExist(int wantedID) {
    List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();
    for (PhotonTrackedTarget target : targets) {
      if (target.getFiducialId() == wantedID) {
        return true;
      }
    }
    return false;
  }

  @Override
  public double getDistanceToTargetInMeters(int wantedID) {
    return getDistanceToTargetInInches(wantedID) * 0.0254;
  }

  @Override
  public double getDistanceToTargetInInches(int wantedID) {
    double range = 0;

    List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();
    for (PhotonTrackedTarget target : targets) {
      if (target.getFiducialId() == wantedID) {
        range = PhotonUtils.calculateDistanceToTargetMeters(m_cameraHeightInMeters,
            m_targetHeightInMeters, m_cameraPitchInRadians,
            Units.degreesToRadians(target.getPitch()));
        SmartDashboard.putNumber("Vision Pitch", target.getPitch());
      }
    }
    return (range / 0.0254) - m_offsetToSwerveModInches;
  }

  @Override
  public double getTargetID() {
    return m_camera.getLatestResult().getBestTarget().getFiducialId();
  }

  @Override
  public boolean doesPhotonVisionExist() {
    return true;
  }

  @Override
  public TargetDetectedAndAngle getTargetDetectedAndAngle(int wantedID, double setPoint) {
    List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();
    for (PhotonTrackedTarget target : targets) {
      if (target.getFiducialId() == wantedID) {
        SmartDashboard.putNumber("Photon Camera Yaw", target.getYaw());
        SmartDashboard.putNumber("Photon Camera ID", target.getFiducialId());
        double offsetAngle = Units
            .radiansToDegrees(Math.asin(m_offset / getDistanceToTargetInInches(wantedID)));
        double yaw = target.getYaw();
        SmartDashboard.putNumber("Yaw", yaw);
        SmartDashboard.putNumber("Photon Offset", offsetAngle);
        return new TargetDetectedAndAngle(yaw + offsetAngle, true);
      }
    }
    return new TargetDetectedAndAngle(setPoint, false);
  }

  public double getTargetAngle(PhotonTrackedTarget target) {

    SmartDashboard.putNumber("Photon Camera Yaw", target.getYaw());
    SmartDashboard.putNumber("Photon Camera ID", target.getFiducialId());
    double offsetAngle = Units.radiansToDegrees(
        Math.asin(m_offset / getDistanceToTargetInInches(target.getFiducialId())));
    double yaw = target.getYaw();
    SmartDashboard.putNumber("Yaw", yaw);
    SmartDashboard.putNumber("Photon Offset", offsetAngle);
    return yaw + offsetAngle;

  }

  @Override
  public TargetDetectedAndDistance getTargetDetectedAndDistance(int wantedID) {
    double distance = getDistanceToTargetInInches(wantedID);
    if (distance <= 0) {
      return new TargetDetectedAndDistance(0, false);
    }
    return new TargetDetectedAndDistance(distance, true);
  }
}
