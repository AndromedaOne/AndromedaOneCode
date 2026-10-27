package frc.robot.sensors.targetDetection;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.sensors.RealSensorBase;

public class RealTargetDetection extends RealSensorBase implements TargetDetectionBase {
  private double m_yaw = 0.0;
  private double m_skew = 0.0;
  private double m_confidence = 0.0;
  private double m_pitch = 0.0;
  private double m_area = 0.0;
  private PhotonCamera m_camera;
  private String sensorName = "TargetDetection";

  public RealTargetDetection(String cameraName) {
    m_camera = new PhotonCamera(cameraName);
  }

  @Override
  protected void periodicUpdate() {
    List<PhotonTrackedTarget> resultList;
    List<PhotonPipelineResult> pipelineResults = m_camera.getAllUnreadResults();
    for (int results = 0; results < pipelineResults.size(); results++) {
      resultList = pipelineResults.get(results).getTargets();
      for (int item = 0; item < resultList.size(); item++) {
        m_skew = resultList.get(item).skew;
        m_yaw = resultList.get(item).yaw;
        // confidence m_confidence = resultList.get(item).
        m_pitch = resultList.get(item).pitch;
        m_area = resultList.get(item).area;

        SmartDashboard.putNumber(sensorName + "/Skew", m_skew);
        SmartDashboard.putNumber(sensorName + "/Yaw", m_yaw);
        SmartDashboard.putNumber(sensorName + "/Confidence", m_confidence);
        SmartDashboard.putNumber(sensorName + "/Pitch", m_pitch);
        SmartDashboard.putNumber(sensorName + "/Area", m_area);
      }

    }
  }

  public double getYaw() {
    return m_yaw;
  }

  public double getSkew() {
    return m_skew;
  }

  public double getConfidence() {
    return m_confidence;
  }

  public double getPitch() {
    return m_pitch;
  }

  public double getArea() {
    return m_area;
  }
}
