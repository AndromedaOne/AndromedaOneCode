package frc.robot.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;
import frc.robot.Robot;
import frc.robot.sensors.SensorsContainer;
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
  private double m_fieldLength;
  private double m_fieldWidth;
  private Alliance m_currentAlliance;
  private AprilTagFieldLayout m_aprilTagFieldLayout;
  private int m_poseAngleDelayCounter = 0;

  StructPublisher<Pose2d> m_posePublisherOdometry = NetworkTableInstance.getDefault()
      .getStructTopic("/OdometryPose", Pose2d.struct).publish();
  StructPublisher<Pose2d> m_posePublisherVision = NetworkTableInstance.getDefault()
      .getStructTopic("/VisionPose", Pose2d.struct).publish();

  public PoseEstimation4905(SwerveDriveKinematics kinematics,
      SwerveModulePosition[] modulePositions) {
    SensorsContainer sensorsContainer = Robot.getInstance().getSensorsContainer();
    m_gyro = sensorsContainer.getGyro();
    m_currentAlliance = AllianceConfig.getCurrentAlliance();
    if (sensorsContainer.hasPhotonVision()) {
      m_photonVision = (sensorsContainer.getPhotonVisionList());
      // when the new field comes out, remember to change this
      if (Config4905.getConfig4905().getSensorConfig()
          .getBoolean("photonvision.useAndyMarkField")) {
        m_aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltAndymark);
      } else {
        m_aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);
      }
      m_fieldLength = m_aprilTagFieldLayout.getFieldLength();
      m_fieldWidth = m_aprilTagFieldLayout.getFieldWidth();
      if (m_currentAlliance == Alliance.Red) {
        m_aprilTagFieldLayout.setOrigin(
            new Pose3d(m_fieldLength, m_fieldWidth, 0, new Rotation3d(0, 0, Math.toRadians(180))));
      }
      PhotonVisionBase localCamera;
      if (m_photonVision.isEmpty()) {
        m_cameraPresent = false;
      }
      if (m_cameraPresent) {
        for (int i = 0; i < m_photonVision.size(); i++) {
          localCamera = m_photonVision.get(i);
          m_robotToCam
              .add(new Transform3d(localCamera.getTranslation3d(), localCamera.getRotation3d()));
          m_poseEstimator.add(new PhotonPoseEstimator(m_aprilTagFieldLayout, m_robotToCam.get(i)));
          m_posePublisherCamera.add(NetworkTableInstance.getDefault()
              .getStructTopic("/CameraPose" + i, Pose2d.struct).publish());
          Trace.getInstance().logInfo("Using Camera " + String.valueOf(i) + "For Pose");
        }
        m_useVisionForPose = Config4905.getConfig4905().getSensorConfig()
            .getBoolean("photonvision.useVisionForPose");

      }
    } else {
      m_cameraPresent = false;
    }
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
    Pose2d previousPose;

    Alliance alliance = AllianceConfig.getCurrentAlliance();
    if (alliance != m_currentAlliance) {
      if (alliance == Alliance.Red) {
        m_aprilTagFieldLayout.setOrigin(
            new Pose3d(m_fieldLength, m_fieldWidth, 0, new Rotation3d(0, 0, Math.toRadians(180))));
      } else {
        m_aprilTagFieldLayout.setOrigin(new Pose3d(0, 0, 0, new Rotation3d(0, 0, 0)));
      }
      m_currentAlliance = alliance;
    }

    previousPose = m_swerveOdometry.getEstimatedPosition();
    localPose = m_swerveOdometry.update(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
        modulePositions);
    m_posePublisherOdometry.set(localPose);
    double xprime = localPose.getX() - previousPose.getX();
    double yprime = localPose.getY() - previousPose.getY();
    double z = Math.sqrt((xprime * xprime) + (yprime * yprime));
    // putting z into meters per second
    z = z / 0.02;
    double degree = Math
        .abs(localPose.getRotation().getDegrees() - previousPose.getRotation().getDegrees());
    // putting degree into degrees per second
    degree = degree / 0.02;
    // on smart dashboard because why not
    SmartDashboard.putNumber("robot velocity", z);
    SmartDashboard.putNumber("robot rotation velocity", degree);

    // NOTE: there is a very specific edge case which messes up the rotation.
    // this edge case requires one of the POV buttons to be held, the robot to have
    // never seen an april tag, and the ending angle to see an april tag.
    // when the robot slows down its rotation, the robot will update the pose before
    // the robot is finished. this messes up the angle a ton. however, due to the
    // unlikely nature of this occuring, it was deemed not necessary to fix.

    // 1.5 is an arbitrary number btw
    // so is 30
    if (m_cameraPresent && z <= 1.5 && degree <= 30) {
      boolean usePose = false;
      for (int i = 0; i < m_poseEstimator.size(); i++) {
        List<PhotonPipelineResult> pipelineResults = m_photonVision.get(i).getPhotonCamera()
            .getAllUnreadResults();
        for (int results = 0; results < pipelineResults.size(); results++) {
          Optional<EstimatedRobotPose> optionalEstimatedPose = Optional.empty();
          optionalEstimatedPose = m_poseEstimator.get(i)
              .estimateCoprocMultiTagPose(pipelineResults.get(results));
          if (optionalEstimatedPose.isEmpty()) {
            optionalEstimatedPose = m_poseEstimator.get(i)
                .estimateLowestAmbiguityPose(pipelineResults.get(results));
          }
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
      SmartDashboard.putNumber("localpose", localPose.getRotation().getDegrees());
      SmartDashboard.putBoolean("UsePose", usePose);
      if (m_updateGyroOffset) {
        m_poseAngleDelayCounter++;
      }
      if (m_useVisionForPose && usePose && m_updateGyroOffset && (m_poseAngleDelayCounter > 40)) {
        double poseAngle = localPose.getRotation().getDegrees();
        Trace.getInstance().logInfo("Setting vision pose offset: " + poseAngle);
        m_gyro.setVisionPoseOffset(poseAngle);
        SmartDashboard.putNumber("visionposeoffset", poseAngle);
        m_updateGyroOffset = false;
      }
    }
    m_posePublisherVision.set(localPose);
    return localPose;
  }
}
