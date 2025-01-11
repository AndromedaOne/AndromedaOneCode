package frc.robot.utils;

import java.util.ArrayList;
import java.util.List;

import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.sensors.photonvision.PhotonVisionBase.AprilTagInfo;

public class PoseEstimation4905 {

  private SwerveDrivePoseEstimator m_swerveOdometry;
  private Gyro4905 m_gyro;
  private ArrayList<PhotonVisionBase> m_photonVision = new ArrayList<PhotonVisionBase>();
  private ArrayList<Transform3d> m_robotToCam = new ArrayList<Transform3d>();
  private ArrayList<PhotonPoseEstimator> m_poseEstimator = new ArrayList<PhotonPoseEstimator>();
  private boolean m_cameraPresent = true;

  public PoseEstimation4905(SwerveDriveKinematics kinematics,
      SwerveModulePosition[] modulePositions) {

    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    m_photonVision = (Robot.getInstance().getSensorsContainer().getPhotonVisionList());
    AprilTagFieldLayout aprilTagFieldLayout = AprilTagFields.k2024Crescendo
        .loadAprilTagLayoutField();
    PhotonVisionBase localCamera;
    if (m_photonVision == null) {
      m_cameraPresent = false;
    }
    if (m_cameraPresent) {
      for (int i = 0; i < m_photonVision.size(); i++) {
        localCamera = m_photonVision.get(i);
        m_robotToCam
            .add(new Transform3d(localCamera.getTranslation3d(), localCamera.getRotation3d()));
        m_poseEstimator.add(new PhotonPoseEstimator(aprilTagFieldLayout,
            PoseStrategy.LOWEST_AMBIGUITY, localCamera.getPhotonCamera(), m_robotToCam.get(i)));
      }
    }
    m_swerveOdometry = new SwerveDrivePoseEstimator(kinematics,
        Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()), modulePositions, new Pose2d());
  }

  public Pose2d getPose() {
    return m_swerveOdometry.getEstimatedPosition();
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
    int bestCamIndex = -1;
    double savedAmbiguity = 100.0;
    localPose = m_swerveOdometry.update(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
        modulePositions);
    if (m_cameraPresent) {
      for (int i = 0; i < m_photonVision.size(); i++) {
        localCamera = m_photonVision.get(i);
        info = localCamera.getAprilTagInfo();
        for (int j = 0; j < info.size(); j++) {
          AprilTagInfo currentTag = info.get(j);
          if (savedAmbiguity > currentTag.ambiguity) {
            savedAmbiguity = currentTag.ambiguity;
            bestCamIndex = i;
          }
        }
      }
    }
    // if the ambiguity is high we don't want to use the cameras because it will be
    // unreliable
    if (savedAmbiguity > 50.0) {
      return localPose;
    }
    localPose = m_poseEstimator.get(bestCamIndex).getReferencePose().toPose2d();
    m_swerveOdometry.addVisionMeasurement(localPose, Timer.getFPGATimestamp());
    return localPose;
  }

}
