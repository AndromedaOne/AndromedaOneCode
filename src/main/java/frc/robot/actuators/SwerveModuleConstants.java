package frc.robot.actuators;

import edu.wpi.first.math.geometry.Rotation2d;

public class SwerveModuleConstants {
  private final int m_driveMotorID;
  private final int m_angleMotorID;
  private final int m_canCoderID;
  private final Rotation2d m_angleOffset;

  public SwerveModuleConstants(int driveMotorID, int angleMotorID, int canCoderID,
      Rotation2d angleOffset) {
    m_driveMotorID = driveMotorID;
    m_angleMotorID = angleMotorID;
    m_canCoderID = canCoderID;
    m_angleOffset = angleOffset;
  }

  public int getDriveMotorID() {
    return m_driveMotorID;
  }

  public int getAngleMotorID() {
    return m_angleMotorID;
  }

  public int getCanCoderID() {
    return m_canCoderID;
  }

  public Rotation2d getAngleOffset() {
    return m_angleOffset;
  }

}
