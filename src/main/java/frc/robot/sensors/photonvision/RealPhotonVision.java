// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.RealSensorBase;

/** Add your docs here. */
public class RealPhotonVision extends RealSensorBase implements PhotonVisionBase {
  private PhotonCamera m_camera;
  private double m_cameraHeight = 12; // Put these into configs at some point
  private double m_targetHeight = 12;
  private double m_cameraPitch = 14;

  @Override
  protected void updateSmartDashboard() {

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
  public double getDistanceToTarget(int wantedID) {
    double range = 0;

    List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();
    for (PhotonTrackedTarget target : targets) {
      if (target.getFiducialId() == wantedID) {
        range = PhotonUtils.calculateDistanceToTargetMeters(m_cameraHeight, m_targetHeight,
            m_cameraPitch, Units.degreesToRadians(target.getPitch()));
      }
    }
    return range;
  }

  @Override
  public double getTargetID() {
    return m_camera.getLatestResult().getBestTarget().getFiducialId();
  }

  @Override
  public boolean doesPhotonVisionExist() {
    return true;
  }

  private class PhotonVisionYawSupplier implements DoubleSupplier {
    private IntSupplier m_wantedID = () -> -1;
    private DoubleSupplier m_setpoint = () -> 0;

    public PhotonVisionYawSupplier(IntSupplier wantedID, DoubleSupplier setpoint) {
      m_wantedID = wantedID;
      m_setpoint = setpoint;
    }

    @Override
    public double getAsDouble() {
      List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();
      for (PhotonTrackedTarget target : targets) {
        if (target.getFiducialId() == m_wantedID.getAsInt()) {
          SmartDashboard.putNumber("Photon Camera Yaw", target.getYaw());
          SmartDashboard.putNumber("Photon Camera ID", target.getFiducialId());
          return target.getYaw();
        }
      }
      return m_setpoint.getAsDouble();
    }

  }

  @Override
  public DoubleSupplier getYaw(IntSupplier wantedID, DoubleSupplier setpoint) {
    return new PhotonVisionYawSupplier(wantedID, setpoint);
  }
}
