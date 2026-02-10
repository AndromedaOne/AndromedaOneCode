// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.ArrayList;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import com.typesafe.config.Config;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;

/** Add your docs here. */
public class RealPhotonVision extends RealSensorBase implements PhotonVisionBase {
  private PhotonCamera m_camera;
  private Config m_config = Config4905.getConfig4905().getSensorConfig();
  private double m_offsetToCenterInInches = 0;
  private double m_offsetToCenterInMeters = 0;
  private double m_offsetToCenterInInchesY = 0;
  private double m_offsetToCenterInMetersY = 0;
  private double m_cameraHeightInInches = 0;
  private double m_cameraHeightInMeters = 0;
  private double m_targetHeightInMeters = 0;
  private double m_cameraRollInDegrees = 0;
  private double m_cameraRollInRadians = 0;
  private double m_cameraPitchInDegrees = 0;
  private double m_cameraPitchInRadians = 0;
  private double m_cameraYawInDegrees = 0;
  private double m_cameraYawInRadians = 0;

  public RealPhotonVision(String cameraName) {
    // pass the name of the camera in
    // pass in photonvision.name
    m_camera = new PhotonCamera(cameraName);
    m_offsetToCenterInInches = m_config
        .getDouble("photonvision." + cameraName + ".cameraOffsetToCenterInInches");
    m_offsetToCenterInMeters = m_offsetToCenterInInches * 0.0254;
    m_offsetToCenterInInchesY = m_config
        .getDouble("photonvision." + cameraName + ".cameraOffsetToCenterInInchesY");
    m_offsetToCenterInMetersY = m_offsetToCenterInInchesY * 0.0254;
    m_cameraHeightInInches = m_config
        .getDouble("photonvision." + cameraName + ".cameraHeightInInches");
    m_cameraHeightInMeters = m_cameraHeightInInches * 0.0254;
    m_cameraRollInDegrees = m_config
        .getDouble("photonvision." + cameraName + ".cameraRollInDegrees");
    m_cameraRollInRadians = Units.degreesToRadians(m_cameraRollInDegrees);
    m_cameraPitchInDegrees = m_config
        .getDouble("photonvision." + cameraName + ".cameraPitchInDegrees");
    m_cameraPitchInRadians = Units.degreesToRadians(m_cameraPitchInDegrees);
    m_cameraYawInDegrees = m_config.getDouble("photonvision." + cameraName + ".cameraYawInDegrees");
    m_cameraYawInRadians = Units.degreesToRadians(m_cameraYawInDegrees);
  }

  @Override
  public List<AprilTagInfo> getAprilTagInfo() {
    ArrayList<AprilTagInfo> info = new ArrayList<>();
    AprilTagInfo localInfo = new AprilTagInfo();
    PhotonTrackedTarget bestTarget;
    List<PhotonTrackedTarget> resultList;

    List<PhotonPipelineResult> pipelineResults = m_camera.getAllUnreadResults();
    for (int results = 0; results < pipelineResults.size(); results++) {
      resultList = pipelineResults.get(results).getTargets();
      for (int item = 0; item < resultList.size(); item++) {
        bestTarget = resultList.get(item);
        localInfo.aprilTagID = bestTarget.fiducialId;
        localInfo.distanceToTarget = getDistanceToTargetInMeters(localInfo.aprilTagID);
        localInfo.angleToTarget = getTargetAngle(bestTarget);
        localInfo.ambiguity = getAmbiguity(bestTarget);
        info.add(localInfo);
      }

    }

    return info;
  }

  public double getAmbiguity(PhotonTrackedTarget target) {
    return target.getPoseAmbiguity();
  }

  public Rotation3d getRotation3d() {
    return new Rotation3d(m_cameraRollInRadians, m_cameraPitchInRadians, m_cameraYawInRadians);
  }

  public Translation3d getTranslation3d() {
    return new Translation3d(m_offsetToCenterInMeters, m_offsetToCenterInMetersY,
        m_cameraHeightInMeters);
  }

  @Override
  protected void periodicUpdate() {

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
      }
    }
    return range / 0.0254;
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
        double offsetAngle = Units.radiansToDegrees(
            Math.asin(m_offsetToCenterInInches / getDistanceToTargetInInches(wantedID)));
        double yaw = target.getYaw();
        return new TargetDetectedAndAngle(yaw + offsetAngle, true);
      }
    }
    return new TargetDetectedAndAngle(setPoint, false);
  }

  public double getTargetAngle(PhotonTrackedTarget target) {
    double offsetAngle = Units.radiansToDegrees(
        Math.asin(m_offsetToCenterInInches / getDistanceToTargetInInches(target.getFiducialId())));
    double yaw = target.getYaw();
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

  @Override
  public PhotonCamera getPhotonCamera() {
    return m_camera;
  }

}
