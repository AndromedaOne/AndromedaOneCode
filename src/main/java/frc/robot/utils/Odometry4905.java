package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import frc.robot.Robot;
import frc.robot.sensors.gyro.Gyro4905;

public class Odometry4905 {
  private SwerveDriveOdometry m_swerveOdometry;
  private Gyro4905 m_gyro;

  public Odometry4905(SwerveDriveKinematics kinematics, SwerveModulePosition[] modulePositions) {
    m_gyro = Robot.getInstance().getSensorsContainer().getGyro();
    m_swerveOdometry = new SwerveDriveOdometry(kinematics,
        Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()), modulePositions);
  }

  public Pose2d getPose() {
    return m_swerveOdometry.getPoseMeters();
  }

  public void resetPosition(SwerveModulePosition[] modulePositions, Pose2d pose) {
    m_swerveOdometry.resetPosition(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
        modulePositions, pose);
  }

  public Pose2d update(SwerveModulePosition[] modulePositions) {
    return m_swerveOdometry.update(Rotation2d.fromDegrees(-1 * m_gyro.getCompassHeading()),
        modulePositions);
  }
}
