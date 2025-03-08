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

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.sensors.RealSensorBase;
import frc.robot.telemetries.Trace;

/** Add your docs here. */
public class RealPhotonVision extends RealSensorBase implements PhotonVisionBase {
  private PhotonCamera m_camera;
  private Config m_config = Config4905.getConfig4905().getSensorConfig();
  private double m_offsetToSwerveModInches = 0;
  private double m_offsetToCenterInInches = 0;
  private double m_offsetToCenterInMeters = 0;
  private double m_offsetToCenterInInchesY = 0;
  private double m_offsetToCenterInMetersY = 0;
  private double m_cameraHeightInInches = 0;
  private double m_cameraHeightInMeters = 0;
  private double m_targetHeightInInches = 0;
  private double m_targetHeightInMeters = 0;
  private double m_cameraRollInDegrees = 0;
  private double m_cameraRollInRadians = 0;
  private double m_cameraPitchInDegrees = 0;
  private double m_cameraPitchInRadians = 0;
  private double m_cameraYawInDegrees = 0;
  private double m_cameraYawInRadians = 0;

  /*
   * public class AprilTagInfo { int aprilTagID; double distanceToTarget; double
   * angleToTarget; double ambiguity; }
   */

  public RealPhotonVision(String cameraName) {
    // pass the name of the camera in
    // pass in photonvision.name
    m_camera = new PhotonCamera(cameraName);
    m_offsetToSwerveModInches = m_config
        .getDouble("photonvision." + cameraName + ".offsetToSwerveModInches");
    m_offsetToCenterInInches = m_config
        .getDouble("photonvision." + cameraName + ".cameraOffsetToCenterInInches");
    m_offsetToCenterInMeters = m_offsetToCenterInInches * 0.0254;
    m_offsetToCenterInInchesY = m_config
        .getDouble("photonvision." + cameraName + ".cameraOffsetToCenterInInchesY");
    m_offsetToCenterInMetersY = m_offsetToCenterInInchesY * 0.0254;
    m_cameraHeightInInches = m_config
        .getDouble("photonvision." + cameraName + ".cameraHeightInInches");
    m_cameraHeightInMeters = m_cameraHeightInInches * 0.0254;
    m_targetHeightInInches = m_config.getDouble("photonvision.targetHeightInInches");
    m_targetHeightInMeters = m_targetHeightInInches * 0.0254;
    m_cameraRollInDegrees = m_config
        .getDouble("photonvision." + cameraName + ".cameraRollInDegrees");
    m_cameraRollInRadians = Units.degreesToRadians(m_cameraRollInDegrees);
    m_cameraPitchInDegrees = m_config
        .getDouble("photonvision." + cameraName + ".cameraPitchInDegrees");
    m_cameraPitchInRadians = Units.degreesToRadians(m_cameraPitchInDegrees);
    m_cameraYawInDegrees = m_config.getDouble("photonvision." + cameraName + ".cameraYawInDegrees");
    m_cameraYawInRadians = Units.degreesToRadians(m_cameraYawInDegrees);
    SmartDashboard.putBoolean("lost target", false);
  }

  @Override
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

  public Rotation3d getRotation3d() {
    return new Rotation3d(m_cameraRollInRadians, m_cameraPitchInRadians, m_cameraYawInRadians);
  }

  public Translation3d getTranslation3d() {
    return new Translation3d(m_offsetToCenterInMeters, m_offsetToCenterInMetersY,
        m_cameraHeightInMeters);
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
        SmartDashboard.putNumber("Photon Camera Yaw", target.getYaw());
        SmartDashboard.putNumber("Photon Camera ID", target.getFiducialId());
        double offsetAngle = Units.radiansToDegrees(
            Math.asin(m_offsetToCenterInInches / getDistanceToTargetInInches(wantedID)));
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
        Math.asin(m_offsetToCenterInInches / getDistanceToTargetInInches(target.getFiducialId())));
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

  @Override
  public PhotonCamera getPhotonCamera() {
    return m_camera;
  }

  @Override
  public TargetDistanceAndAngle computeDistanceAndAngle(int wantedID, boolean useTrace,
      boolean useLeft) {
    // refer to the piece of paper
    List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();

    // 6.5 is the distance between the april tag and the pipe

    // m_offsetToSwerveModInches is c - distance from camera to bumpers
    // m_cameraOffsetToCenterInches is 11.69

    // getting b - the distance from the camera to the reef
    // getting e - the horizontal distance from the camera to the april tag
    double b = 0;
    double e = 0;

    boolean hasTarget = false;

    for (PhotonTrackedTarget target : targets) {
      if (target.getFiducialId() == wantedID) {
        Transform3d wantedCamera = target.getBestCameraToTarget();
        SmartDashboard.putNumber("best target x", wantedCamera.getX());
        SmartDashboard.putNumber("best target y", wantedCamera.getY());
        SmartDashboard.putNumber("best target z", wantedCamera.getZ());
        b = wantedCamera.getX() / Math.cos(m_cameraPitchInRadians);
        e = -wantedCamera.getY();
        // changing b and e to be in inches
        b = b / 0.0254;
        e = e / 0.0254;

        hasTarget = true;

      }
    }

    // getting g - the distance between the camera and the pipe
    double g = 0;
    g = e + 6.5;

    // getting h - the distance between the bumpers and the reef
    double h = 0;
    h = b - m_offsetToSwerveModInches; // b - c

    // getting j - the horizontal distance between the center of the robot and the
    // pipe
    // if j is negative it will trigger some logic to invert theta
    double j = 0;
    boolean reverseTheta = false;
    j = Math.abs(m_offsetToCenterInInches) - g;
    if (useLeft) {
      j += 13;
    }
    if (j < 0) {
      reverseTheta = true;
    }

    // getting x - the actual distance between the center of the robot and the pipe
    double x = 0;
    x = Math.sqrt((Math.pow(h, 2)) + (Math.pow(j, 2)));

    // getting theta - the angle between the center of the robot and the pipe
    // if j was less than 0 theta will be reversed
    double theta = 0;
    if (reverseTheta) {
      theta = 360 - (90 - Units.radiansToDegrees(Math.asin(h / x)));
    } else {
      theta = 90 - Units.radiansToDegrees(Math.asin(h / x));
    }

    if (useTrace) {
      Trace.getInstance().logInfo("b: " + b);
      Trace.getInstance().logInfo("e: " + e);
      Trace.getInstance().logInfo("g: " + g);
      Trace.getInstance().logInfo("h: " + h);
      Trace.getInstance().logInfo("j: " + j);
      Trace.getInstance().logInfo("reverse theta: " + reverseTheta);
      Trace.getInstance().logInfo("x: " + x);
      Trace.getInstance().logInfo("theta: " + theta);
    }
    SmartDashboard.putNumber("b value", b);
    SmartDashboard.putNumber("e value", e);
    SmartDashboard.putNumber("g value", g);
    SmartDashboard.putNumber("h value", h);
    SmartDashboard.putNumber("j value", j);
    SmartDashboard.putBoolean("reverseTheta", reverseTheta);
    SmartDashboard.putNumber("x value", x);
    SmartDashboard.putNumber("theta", theta);
    return new TargetDistanceAndAngle(x, theta, hasTarget);
  }

}
