package frc.robot.actuators;

public class SwerveModuleConstants {
  private final int m_driveMotorID;
  private final int m_angleMotorID;

  public SwerveModuleConstants(int driveMotorID, int angleMotorID) {
    m_driveMotorID = driveMotorID;
    m_angleMotorID = angleMotorID;
  }

  public int getDriveMotorID() {
    return m_driveMotorID;
  }

  public int getAngleMotorID() {
    return m_angleMotorID;
  }

}
