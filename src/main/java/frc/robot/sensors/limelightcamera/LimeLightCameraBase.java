package frc.robot.sensors.limelightcamera;

public abstract class LimeLightCameraBase {
  public abstract double horizontalRadiansToTarget();

  public abstract double verticalRadiansToTarget();

  public abstract double distanceToTarget(double targetHeight);

  public abstract double distanceToPowerPort();

  public abstract void setPipeline(int pipelineNumber);
}