package frc.robot.sensors.targetDetection;

import java.util.function.DoubleSupplier;

public class MockTargetDetection implements TargetDetectionBase {

  public double getYaw() {
    return 0.0;
  }

  public double getSkew() {
    return 0.0;
  }

  public double getConfidence() {
    return 0.0;
  }

  public double getPitch() {
    return 0.0;
  }

  public double getArea() {
    return 0.0;
  }

  public DoubleSupplier getYawAsDoubleSupplier() {
    return () -> getYaw();
  }
}
