package frc.robot.utils;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.Config4905;

public class FieldConstants {
  private AprilTagFieldLayout m_aprilTagFieldLayout;
  private Pose2d m_hubPose;

  public FieldConstants() {
    double hubX;
    double hubY;
    if (Config4905.getConfig4905().getSensorConfig().getBoolean("photonvision.useAndyMarkField")) {
      m_aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltAndymark);
    } else {
      m_aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);
    }
    hubX = m_aprilTagFieldLayout.getTagPose(26).get().getX() + Units.inchesToMeters(47) / 2;
    hubY = m_aprilTagFieldLayout.getFieldWidth() / 2;
    m_hubPose = new Pose2d(hubX, hubY, new Rotation2d());
  }

  public AprilTagFieldLayout getFieldLayout() {
    return m_aprilTagFieldLayout;
  }

  public Pose2d getHubPose() {
    return m_hubPose;
  }
}
