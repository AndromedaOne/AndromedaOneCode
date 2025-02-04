package frc.robot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;
import frc.robot.sensors.photonvision.PhotonVisionBase;
import frc.robot.telemetries.Trace;

public class PoseEstimation4905 {

  private SwerveDrivePoseEstimator m_swerveOdometry;
  private Gyro4905 m_gyro;
  private ArrayList<PhotonVisionBase> m_photonVision = new ArrayList<PhotonVisionBase>();
  private ArrayList<Transform3d> m_robotToCam = new ArrayList<Transform3d>();
  private ArrayList<PhotonPoseEstimator> m_poseEstimator = new ArrayList<PhotonPoseEstimator>();
  private boolean m_cameraPresent = true;
  private boolean m_useVisionForPose = true;
  private boolean m_updateGyroOffset = true;
  private ArrayList<StructPublisher<Pose2d>> m_posePublisherCamera = new ArrayList<StructPublisher<Pose2d>>();

  StructPublisher<Pose2d> m_posePublisherOdometry = NetworkTableInstance.getDefault()
      .getStructTopic("/OdometryPose", Pose2d.struct).publish();
  StructPublisher<Pose2d> m_posePublisherVision = NetworkTableInstance.getDefault()
      .getStructTopic("/VisionPose", Pose2d.struct).publish();

  public PoseEstimation4905(SwerveDriveKinematics kinematics,
      SwerveModulePosition[] modulePositions) {

    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    m_photonVision = (Robot.getInstance().getSensorsContainer().getPhotonVisionList());
    AprilTagFieldLayout aprilTagFieldLayout = AprilTagFields.k2025Reefscape
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
            PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, m_robotToCam.get(i)));
        m_posePublisherCamera.add(NetworkTableInstance.getDefault()
            .getStructTopic("/CameraPose" + (i + 1), Pose2d.struct).publish());
        Trace.getInstance().logInfo(m_robotToCam.get(i).toString());
      }
    }
    m_useVisionForPose = Config4905.getConfig4905().getSensorConfig()
        .getBoolean("photonvision.useVisionForPose");
    m_swerveOdometry = new SwerveDrivePoseEstimator(kinematics,
        Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()), modulePositions, new Pose2d());
  }

  public Pose2d getPose() {
    return m_swerveOdometry.getEstimatedPosition();
  }

  public void setPose() {
    m_swerveOdometry.resetPose(new Pose2d(SmartDashboard.getNumber("Set Pose X", 0),
        SmartDashboard.getNumber("Set Pose Y", 0),
        new Rotation2d(SmartDashboard.getNumber("Set Pose Angle", 0))));
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

    localPose = m_swerveOdometry.update(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
        modulePositions);
    m_posePublisherOdometry.set(localPose);

    if (m_cameraPresent) {
      boolean usePose = false;
      for (int i = 0; i < m_poseEstimator.size(); i++) {
        // get latest result is deprecated and needs to be replaced with get all unread
        // results which returns a list of results
        List<PhotonPipelineResult> pipelineResults = m_photonVision.get(i).getPhotonCamera()
            .getAllUnreadResults();
        for (int results = 0; results < pipelineResults.size(); results++) {
          final Optional<EstimatedRobotPose> optionalEstimatedPose = m_poseEstimator.get(i)
              .update(pipelineResults.get(results));
          if (optionalEstimatedPose.isPresent()) {
            final EstimatedRobotPose estimatedPose = optionalEstimatedPose.get();
            usePose = true;
            for (int j = 0; j < estimatedPose.targetsUsed.size(); j++) {
              if (estimatedPose.targetsUsed.get(j).getPoseAmbiguity() > 0.1) {
                usePose = false;
              }
            }
            if (usePose && m_useVisionForPose) {
              m_swerveOdometry.addVisionMeasurement(estimatedPose.estimatedPose.toPose2d(),
                  estimatedPose.timestampSeconds);
            }
            if (usePose) {
              m_posePublisherCamera.get(i).set(estimatedPose.estimatedPose.toPose2d());
            }
          }
        }
      }
      localPose = m_swerveOdometry.getEstimatedPosition();
      if (m_useVisionForPose && usePose && m_updateGyroOffset) {
        Trace.getInstance()
            .logInfo("Setting vision pose offset: " + localPose.getRotation().getDegrees());
        m_gyro.setVisionPoseOffset(localPose.getRotation().getDegrees());
        m_updateGyroOffset = false;
      }
    }

    m_posePublisherVision.set(localPose);
    return localPose;
  }

  public enum RegionsForPose {
    NORTHEAST, NORTH, NORTHWEST, SOUTHWEST, SOUTH, SOUTHEAST
  }

  public RegionsForPose getRegion() {
    double x = m_swerveOdometry.getEstimatedPosition().getX() - 4.489323;
    double y = m_swerveOdometry.getEstimatedPosition().getY() - 4.0259127;
    double z = Math.sqrt((x * x) + (y * y));
    double theta = Math.asin(x / z);
    if (theta < 60 && y > 0) {
      return RegionsForPose.NORTHEAST;
    } else if (theta < 120 && y > 0) {
      return RegionsForPose.NORTH;
    } else if (theta < 180 && y > 0) {
      return RegionsForPose.NORTHWEST;
    } else if (theta < 60 && y <= 0) {
      return RegionsForPose.SOUTHWEST;
    } else if (theta < 120 && y <= 0) {
      return RegionsForPose.SOUTH;
    } else {
      return RegionsForPose.SOUTHEAST;
    }
  }

}
