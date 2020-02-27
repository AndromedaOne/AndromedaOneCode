package frc.robot.sensors.limelightcamera;

import com.typesafe.config.Config;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Config4905;

public class RealLimelightCamera extends LimeLightCameraBase {
  protected Config m_config = Config4905.getConfig4905().getSensorConfig();
  protected NetworkTable m_limelightTable;
  protected double m_cameraHeight = m_config.getDouble("limelight.cameraHeight");
  protected double m_cameraAngle = m_config.getDouble("limelight.cameraAngleRadians");
  protected double previousXValue = 0.0;
  protected double previousYValue = 0.0;

  public RealLimelightCamera() {
    m_limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  @Override
  public double horizontalDegreesToTarget() {
    if (targetLock()) {
      previousXValue = (m_limelightTable.getEntry("tx").getDouble(0.0));
    }
    return previousXValue;
  }

  @Override
  public double verticalRadiansToTarget() {
    if (targetLock()) {
      previousYValue = (m_limelightTable.getEntry("ty").getDouble(0.0));
    }
    return Math.toRadians(previousYValue);
  }

  @Override
  public double distanceToTarget(double targetHeight) {
    return (targetHeight - m_cameraHeight) / Math.tan(verticalRadiansToTarget() + m_cameraAngle);
  }

  @Override
  public double distanceToPowerPort() {
    return distanceToTarget(82.5) - m_config.getDouble("limelight.cameraDistToFrame");
  }

  @Override
  public void setPipeline(int pipelineNumber) {
    m_limelightTable.getEntry("pipeline").setNumber(pipelineNumber);
  }

  @Override
  public void updateSmartDashboardReadings() {
    SmartDashboard.putNumber("LimeAngleToTurn", horizontalDegreesToTarget());
    SmartDashboard.putNumber("Vertical Radians To Target", verticalRadiansToTarget());
    SmartDashboard.putNumber("Distance To Target", distanceToPowerPort());
  }

  @Override
  public boolean targetLock() {
    return m_limelightTable.getEntry("tv").getDouble(0.0) != 0.0;
  }
}