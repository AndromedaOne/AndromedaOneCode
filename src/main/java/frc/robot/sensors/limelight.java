package frc.robot.sensors;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class limelight {
  NetworkTable limelightTable;
  double cameraHeight = 0.0;

  private limelight() {
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
    return (targetHeight - cameraHeight) / Math.tan(verticalRadiansToTarget());
  }

  public double distanceToPowerPort() {
    return 0.0;
  }

  public void setPipeline(int pipelineNumber) {
    limelightTable.getEntry("pipeline").setNumber(pipelineNumber);
  }
}