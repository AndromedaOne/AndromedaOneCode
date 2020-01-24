package frc.robot.sensors.anglesensor;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import frc.robot.sensors.SensorBase;

public abstract class AngleSensor extends SensorBase implements PIDSource {

  public abstract double getAngle();

  public abstract void reset();

  @Override
  public void setPIDSourceType(PIDSourceType pidSource) {

  }

  @Override
  public PIDSourceType getPIDSourceType() {
    return PIDSourceType.kDisplacement;
  }

  @Override
  public double pidGet() {
    return getAngle();
  }
}