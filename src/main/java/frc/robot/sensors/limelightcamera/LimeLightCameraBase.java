package frc.robot.sensors.limelightcamera;

public interface LimeLightCameraBase {
  public double horizontalDegreesToTarget();

  public double verticalRadiansToTarget();

  public double distanceToTarget(double targetHeight);

  public double distanceToPowerPort();

  public void setPipeline(int pipelineNumber);

  public boolean targetLock();

  public void enableLED();

  public void disableLED();

  public boolean doesLimeLightExist();
}