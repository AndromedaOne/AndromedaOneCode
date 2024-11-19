package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;

public class PoseEstimation4905 {
  private SwerveDriveOdometry m_swerveOdometry;
  private Gyro4905 m_gyro;

  public PoseEstimation4905(SwerveDriveKinematics kinematics,
      SwerveModulePosition[] modulePositions) {
    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
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
    // get camera info - calculate april tags and where tf you at
    Pose2d localPose;
    localPose = m_swerveOdometry.update(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
        modulePositions);
    return localPose;
  }
}
