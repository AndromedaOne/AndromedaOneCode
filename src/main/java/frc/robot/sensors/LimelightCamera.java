package frc.robot.sensors;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimelightCamera {
  NetworkTable limelightTable;
  double cameraHeight = 23.0;
  private static LimelightCamera instance;
  double cameraAngle = 0.59;

  public static synchronized LimelightCamera getInstance() {
    if (instance == null) {
      instance = new LimelightCamera();
    }
    return instance;
  }

  private LimelightCamera() {
    limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
  }

  public double horizontalRadiansToTarget() {
    if (limelightTable.getEntry("tv").getDouble(0.0) == 0.0) {
      return Double.NaN;
    } else {
      return Math.toRadians(limelightTable.getEntry("tx").getDouble(0.0));
    }

  }

  public double verticalRadiansToTarget() {
    if (limelightTable.getEntry("tv").getDouble(0.0) == 0.0) {
      return Double.NaN;
    } else {
      return Math.toRadians(limelightTable.getEntry("ty").getDouble(0.0));
    }

  }

  public double distanceToTarget(double targetHeight) {
    return (targetHeight - cameraHeight) / Math.tan(verticalRadiansToTarget() + cameraAngle);
  }

  public double distanceToPowerPort() {
    return 0.0;
  }

  public void setPipeline(int pipelineNumber) {
    limelightTable.getEntry("pipeline").setNumber(pipelineNumber);
  }
}