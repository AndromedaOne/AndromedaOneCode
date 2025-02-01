package frc.robot.utils;

import com.typesafe.config.Config;

import frc.robot.Config4905;
import frc.robot.sensors.photonvision.PhotonVisionBase.AprilTagInfo;

public class AprilTagContainer {

  private class PoseAprilTags {
    AprilTagInfo info;
    double targetHeight;
    // TODO: add x/y cords for april tags
    boolean valid;
  }

  private Config m_config = Config4905.getConfig4905().getSensorConfig();
  private PoseAprilTags[] m_poseTags;
  private int m_numberOfAprilTags;

  public AprilTagContainer() {
    m_numberOfAprilTags = m_config.getInt("Fieldmap.numberOfAprilTags");
    m_poseTags = new PoseAprilTags[m_numberOfAprilTags + 1];
    for (int i = 1; i <= m_numberOfAprilTags; i++) {
      m_poseTags[i].targetHeight = m_config.getDouble("Fieldmap.targetHeightInInches_" + i);
    }
  }
}
