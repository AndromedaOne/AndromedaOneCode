// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.sensors.photonvision;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

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
  private final double m_offsetToSwerveModInches = m_config
      .getDouble("photonvision.offsetToSwerveModInches");
  private final double m_cameraHeightInInches = m_config
      .getDouble("photonvision.cameraHeightInInches");
  private final double m_cameraHeightInMeters = m_cameraHeightInInches * 0.0254;
  private final double m_targetHeightInInches = m_config
      .getDouble("photonvision.targetHeightInInches");
  private final double m_targetHeightInMeters = m_targetHeightInInches * 0.0254;
  private final double m_cameraPitchInDegrees = m_config
      .getDouble("photonvision.cameraPitchInDegrees");
  private final double m_cameraPitchInRadians = Units.degreesToRadians(m_cameraPitchInDegrees);

  public RealPhotonVision() {
    m_camera = new PhotonCamera(m_config.getString("photonvision.cameraName"));
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
    SmartDashboard.putNumber("Photon Vision Range", getDistanceToTargetInInches(4));
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

  private class PhotonVisionYawSupplier implements DoubleSupplier {
    private IntSupplier m_wantedID = () -> -1;
    private DoubleSupplier m_setpoint = () -> 0;
    private double m_counter = 0;
    private double m_previousYaw = 0;

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
          m_previousYaw = target.getYaw();
          m_counter = 0;
          return target.getYaw();
        }
      }
      m_counter++;
      if (m_counter >= 8) {
        return m_setpoint.getAsDouble();
      }
      return m_previousYaw;
    }

  }

  @Override
  public DoubleSupplier getYaw(IntSupplier wantedID, DoubleSupplier setpoint) {
    return new PhotonVisionYawSupplier(wantedID, setpoint);
  }
}
