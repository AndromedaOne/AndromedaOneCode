package frc.robot.sensors;

import com.typesafe.config.Config;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Config4905;

public class LimelightCamera {
  protected Config m_config = Config4905.getConfig4905().getSensorConfig();
  protected NetworkTable m_limelightTable;
  protected double m_cameraHeight = m_config.getDouble("limelight.cameraHeight");
  private static LimelightCamera instance;
  protected double m_cameraAngle = m_config.getDouble("limelight.cameraAngleRadians");

  public static synchronized LimelightCamera getInstance() {
    if (instance == null) {
      instance = new LimelightCamera();
    }
    return instance;
  }

  private LimelightCamera() {
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  public double horizontalRadiansToTarget() {
    if (m_limelightTable.getEntry("tv").getDouble(0.0) == 0.0) {
      return Double.NaN;
    } else {
      return Math.toRadians(m_limelightTable.getEntry("tx").getDouble(0.0));
    }

  }

  public double verticalRadiansToTarget() {
    if (m_limelightTable.getEntry("tv").getDouble(0.0) == 0.0) {
      return Double.NaN;
    } else {
      return Math.toRadians(m_limelightTable.getEntry("ty").getDouble(0.0));
    }

  }

  public double distanceToTarget(double targetHeight) {
    return (targetHeight - m_cameraHeight) / Math.tan(verticalRadiansToTarget() + m_cameraAngle);
  }

  public double distanceToPowerPort() {
    return 0.0;
  }

  public void setPipeline(int pipelineNumber) {
    m_limelightTable.getEntry("pipeline").setNumber(pipelineNumber);
  }
}