package frc.robot.sensors.targetDetection;

public interface TargetDetectionBase {
  public double getYaw();

  public double getPitch();

  public double getConfidence();

  public double getSkew();

  public double getArea();
}