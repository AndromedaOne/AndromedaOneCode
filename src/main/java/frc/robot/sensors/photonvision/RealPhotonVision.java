// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.List;
import java.util.function.DoubleSupplier;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.RealSensorBase;

/** Add your docs here. */
public class RealPhotonVision extends RealSensorBase implements PhotonVisionBase {
  private PhotonCamera m_camera;
  private PhotonPipelineResult m_latestResult;
  private double m_cameraHeight = 12; // Put these into configs at some point
  private double m_targetHeight = 12;
  private double m_cameraPitch = 14;

  @Override
  protected void updateSmartDashboard() {
    SmartDashboard.putNumber("Photon Camera Yaw",
        m_camera.getLatestResult().getBestTarget().getYaw());
  }

  

  @Override
  public boolean doesTargetExist() {
    m_latestResult = m_camera.getLatestResult();
    if (m_latestResult.hasTargets()) {
      return true;
    }
    return false;
  }

  @Override
  public double getDistanceToTarget() {
    double range = 0;
    if (doesTargetExist()) {
      range = PhotonUtils.calculateDistanceToTargetMeters(m_cameraHeight, m_targetHeight,
          m_cameraPitch, Units.degreesToRadians(m_latestResult.getBestTarget().getPitch()));
    }
    return range;
  }

  @Override
  public double getTargetID() {
    return m_camera.getLatestResult().getBestTarget().getFiducialId();
  }

  private class PhotonVisionYawSupplier {
    private int m_wantedID = -1;
    public PhotonVisionYawSupplier(int wantedID) {
      m_wantedID = wantedID;
    }
    public Pair<int, double> getYawDoubleSupplier() {
      List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();
      for (PhotonTrackedTarget target : targets) {
        if (target.getFiducialId() == m_wantedID) {
          return () -> target.getYaw();
        }
      }
      return () -> m_camera.getLatestResult().getBestTarget().getYaw();
    }
  }
}
