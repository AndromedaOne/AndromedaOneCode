package frc.robot.sensors.distanceSensor.ultrasonicsensor;

import frc.robot.sensors.distanceSensor.DistanceSensorBase;

public class MockUltrasonicSensor implements DistanceSensorBase {

  @Override
  public double getDistance_Inches() {
    return 0;
  }

  @Override
  public double getDistance_mm() {
    return 0;
  }
}