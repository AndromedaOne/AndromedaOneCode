package frc.robot.sensors.distanceSensor.pwfTofDistanceSensor;

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
}
