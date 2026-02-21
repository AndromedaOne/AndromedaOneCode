package frc.robot.sensors.distanceSensor.pwfTofDistanceSensor;

import java.util.function.DoubleSupplier;

import frc.robot.sensors.distanceSensor.DistanceSensorBase;

public class MockpwfTofDistanceSensor implements DistanceSensorBase {

  @Override
  public double getDistance_mm() {
    return 0;
  }

  @Override
  public double getDistance_Inches() {
    return 0;
  }

  @Override
  public DoubleSupplier getDistanceInchesAsSupplier() {
    return () -> 0;
  }

  @Override
  public DoubleSupplier getFacingAngle() {
    return () -> 0;
  }

  @Override
  public boolean isSensorDetecting() {
    return false;
  }

}
