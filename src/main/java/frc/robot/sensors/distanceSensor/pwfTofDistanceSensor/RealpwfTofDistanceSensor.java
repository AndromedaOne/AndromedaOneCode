package frc.robot.sensors.distanceSensor.pwfTofDistanceSensor;

import frc.robot.sensors.RealSensorBase;
import frc.robot.sensors.distanceSensor.DistanceSensorBase;

public class RealpwfTofDistanceSensor extends RealSensorBase implements DistanceSensorBase {

  public void pwfTofDistanceSensor() {

  }

  @Override
  protected void updateSmartDashboard() {
    throw new UnsupportedOperationException("Unimplemented method 'updateSmartDashboard'");
  }

  @Override
  public double getDistance() {
    return double tofdistance_mm = tof.getRange();
  }
}
