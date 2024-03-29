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
  private final double m_consecutiveFramesWithoutTarget = 16;
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
  private double m_offset = m_config.getDouble("photonvision.cameraOffsetToCenterInInches");

  public RealPhotonVision() {
    m_camera = new PhotonCamera(m_config.getString("photonvision.cameraName"));
    SmartDashboard.putBoolean("lost target", false);
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

public TargetDetectedAndAngle getTargetDetectedAndAngle(int wantedID, double setPoint) {
      List<PhotonTrackedTarget> targets = m_camera.getLatestResult().getTargets();
      for (PhotonTrackedTarget target : targets) {
        if (target.getFiducialId() == wantedID) {
          SmartDashboard.putNumber("Photon Camera Yaw", target.getYaw());
          SmartDashboard.putNumber("Photon Camera ID", target.getFiducialId());
          double offsetAngle = Units.radiansToDegrees(
              Math.asin(m_offset / getDistanceToTargetInInches(wantedID)));
          double yaw = target.getYaw();
          SmartDashboard.putNumber("Yaw", yaw);
          SmartDashboard.putNumber("Photon Offset", offsetAngle);
          return new TargetDetectedAndAngle(yaw + offsetAngle, true);
        } 
      }
      return new TargetDetectedAndAngle(setPoint, false);
    }

  public class PhotonVisionYawSupplier implements DoubleSupplier {
    private IntSupplier m_wantedID = () -> -1;
    private DoubleSupplier m_setpoint = () -> 0;
    private double m_counter = 0;
    private double m_previousYaw = 0;
    

    public PhotonVisionYawSupplier(IntSupplier wantedID, DoubleSupplier setpoint) {
      m_wantedID = wantedID;
      m_setpoint = setpoint;
      SmartDashboard.putBoolean("lost target", true);
    }

    @Override
    public double getAsDouble() {
      TargetDetectedAndAngle detectedAnAngle = getTargetDetectedAndAngle(m_wantedID.getAsInt(), 
        m_setpoint.getAsDouble());
      if (detectedAnAngle.getDetected()) {
        m_counter = 0;
        m_previousYaw = detectedAnAngle.getAngle();
        return m_previousYaw;
      }
      m_counter++;
      if (m_counter >= m_consecutiveFramesWithoutTarget) {
        SmartDashboard.putBoolean("lost target", true);
        return m_setpoint.getAsDouble();
      }
      return m_previousYaw;
    }

  }

  @Override
  public DoubleSupplier getYaw(IntSupplier wantedID, DoubleSupplier setpoint) {
    return new PhotonVisionYawSupplier(wantedID, setpoint);
  }
  
  public class AngleToTargetDoubleSupplier {
    private double m_angle = 0;

    public AngleToTargetDoubleSupplier (double angle) {
    m_angle = angle;
    }
    public double getAngle() {
      return m_angle;
    }

    public void setAngle(double angle) {
      m_angle = angle;
    } 
  }
  public class TargetDetectedAndAngle {
    private double m_angle = 0;
    private boolean m_detected = false;

    public TargetDetectedAndAngle (double angle, boolean detected) {
      m_angle = angle;
      m_detected = detected;

    }

    public double getAngle() {
      return m_angle;
    }

    public boolean getDetected() {
      return m_detected;
    }

    public void setAngle(double angle) {
      m_angle = angle;
    }
  }
    @Override
  public PhotonVisionYawSupplier getPhotonVisionSupplier(IntSupplier wantedID, DoubleSupplier setpoint) {
    return(new PhotonVisionYawSupplier(wantedID, setpoint));
  }
}
