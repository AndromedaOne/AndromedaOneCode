package frc.robot.sensors.limelightcamera;

import com.typesafe.config.Config;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.Config4905;

public class RealLimelightCamera extends LimeLightCameraBase {
  protected Config m_config = Config4905.getConfig4905().getSensorConfig();
  protected NetworkTable m_limelightTable;
  protected double m_cameraHeight = m_config.getDouble("limelight.cameraHeight");
  protected double m_cameraAngle = m_config.getDouble("limelight.cameraAngleRadians");

  public RealLimelightCamera() {
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  @Override
  public double horizontalRadiansToTarget() {
    if (m_limelightTable.getEntry("tv").getDouble(0.0) == 0.0) {
      return Double.NaN;
    } else {
      return Math.toRadians(m_limelightTable.getEntry("tx").getDouble(0.0));
    }

  }

  @Override
  public double verticalRadiansToTarget() {
    if (m_limelightTable.getEntry("tv").getDouble(0.0) == 0.0) {
      return Double.NaN;
    } else {
      return Math.toRadians(m_limelightTable.getEntry("ty").getDouble(0.0));
    }

  }

  @Override
  public double distanceToTarget(double targetHeight) {
    return (targetHeight - m_cameraHeight) / Math.tan(verticalRadiansToTarget() + m_cameraAngle);
  }

  @Override
  public double distanceToPowerPort() {
    return 0.0;
  }

  @Override
  public void setPipeline(int pipelineNumber) {
    m_limelightTable.getEntry("pipeline").setNumber(pipelineNumber);
  }

  @Override
  public void setLightState(boolean isOn) {
    m_limelightTable.getEntry("ledMode").setNumber(isOn?3:0);
  }

  
}