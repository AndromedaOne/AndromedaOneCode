package frc.robot.utils;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.PhotonVisionBase.AprilTagInfo;

public class PoseEstimation4905 {
  private class PoseAprilTags {
    AprilTagInfo info;
    boolean valid;
  }

  private SwerveDriveOdometry m_swerveOdometry;
  private Gyro4905 m_gyro;
  private ArrayList<PhotonVisionBase> m_photonVision;
  private PoseAprilTags[] m_poseTags; // TODO: allocate to correct number of Tags

  public PoseEstimation4905(SwerveDriveKinematics kinematics,
      SwerveModulePosition[] modulePositions) {

    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    m_photonVision = (Robot.getInstance().getSensorsContainer().getPhotonVisionList());
    m_swerveOdometry = new SwerveDriveOdometry(kinematics,
        Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()), modulePositions);
  }

  public Pose2d getPose() {
    return m_swerveOdometry.getPoseMeters();
  }

  // This function is used to reset the position returns if position has been
  // reset or not
  public boolean resetPosition(SwerveModulePosition[] modulePositions, Pose2d pose) {
    if (m_gyro.getIsCalibrated()) {
      m_swerveOdometry.resetPosition(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
          modulePositions, pose);
      return true;
    } else {
      return false;
    }
  }

  public Pose2d update(SwerveModulePosition[] modulePositions) {
    // where we collect the camera info
    // get camera info - calculate april tags and where you at
    Pose2d localPose;
    PhotonVisionBase localCamera;
    List<AprilTagInfo> info;
    localPose = m_swerveOdometry.update(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
        modulePositions);
    for (int i = 0; i < m_photonVision.size(); i++) {
      localCamera = m_photonVision.get(i);
      info = localCamera.getAprilTagInfo();
      for (int j = 0; j < info.size(); j++) {

      }
    }
    return localPose;
  }

}
