package frc.robot.sensors.targetDetection;

import java.util.function.DoubleSupplier;

public interface TargetDetectionBase {
  public double getYaw();

  public double getPitch();

  public double getConfidence();

  public double getSkew();

  public double getArea();

  public DoubleSupplier getYawAsDoubleSupplier();

}